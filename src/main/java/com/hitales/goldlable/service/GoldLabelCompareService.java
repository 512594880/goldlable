package com.hitales.goldlable.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hitales.goldlable.Entity.GoldLabelEntity;
import com.hitales.goldlable.Entity.OneStructEntity;
import com.hitales.goldlable.repository.GoldLabelRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by wangxi on 18/9/5.
 */
@Service
public class GoldLabelCompareService {
    @Autowired
    private StructService structService;

    @Autowired
    private GoldLabelRepository goldLabelRepository;

    public void compare(List<String> types){
        for (String type:types) {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            XSSFSheet xssfSheet = xssfWorkbook.createSheet();
            List<GoldLabelEntity> goldLabelEntityList = goldLabelRepository.findByType(type);
            for (int i = 0; i < goldLabelEntityList.size(); i++) {
                System.out.println("份数："+i);
                GoldLabelEntity goldLabelEntity = goldLabelEntityList.get(i);
                String context = goldLabelEntity.getContext();
                //调用结构化
                OneStructEntity oneStructEntity = new OneStructEntity();
                oneStructEntity.setPatientId(goldLabelEntity.getPatientId());
                oneStructEntity.setRecordId(goldLabelEntity.getRecordId());
                oneStructEntity.setText(context);
                JSONObject data;
                try {
                    data = structService.doStruct(oneStructEntity);
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }

                //获取计分字段数量
                int count = (int)goldLabelEntity.getList().entrySet().stream().filter(entry -> entry.getValue() != null && !"".equals(entry.getValue())).count();

                //获取msdata
                JSONObject msdata = data.getJSONObject("msdata");
                //获取实体数据
                JSONArray entity = msdata.getJSONArray(type);
                HashMap<String,String> result = dobidui(entity,goldLabelEntity,count);
                //往表格中添加数据
                addxssfSheet(xssfSheet,goldLabelEntity,result);
            }

            try {
                FileOutputStream fos = new FileOutputStream("./OriginExcel/"+type+".xlsx");
                xssfWorkbook.write(fos);
                fos.close();
                xssfWorkbook.close();
                System.out.println("成功");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void addxssfSheet(XSSFSheet xssfSheet, GoldLabelEntity goldLabelEntity, HashMap<String, String> result) {
        List<String> heading = new ArrayList<>();
        heading.add("recordId");
        heading.add("patientId");
        heading.add("上下文");
        goldLabelEntity.getList().forEach((s, s2) -> heading.add(s));
        if (xssfSheet.getLastRowNum() < 1){
            Row row = xssfSheet.createRow(0);
            heading.forEach(s ->row.createCell(row.getLastCellNum() == -1?0:row.getLastCellNum()).setCellValue(s));
            row.createCell(heading.size()).setCellValue("比对得分");
        }

        //第一行 为金标所在行
        Row row = addRowGuDing(xssfSheet,goldLabelEntity);
        heading.stream().skip(3).forEach(s -> row.createCell(row.getLastCellNum()).setCellValue(goldLabelEntity.getList().get(s)));
        row.createCell(row.getLastCellNum()).setCellValue(result.get("比对得分"));

        //第一行 为结构化结果所在行
        Row row2 = addRowGuDing(xssfSheet,goldLabelEntity);
        heading.stream().skip(3).forEach(s -> row2.createCell(row2.getLastCellNum()).setCellValue(result.get(s) == null?"":result.get(s)));
        row2.createCell(row2.getLastCellNum()).setCellValue(result.get("比对得分"));


    }

    //添加固定字段
    private Row addRowGuDing(XSSFSheet xssfSheet, GoldLabelEntity goldLabelEntity) {
        Row row = xssfSheet.createRow(xssfSheet.getLastRowNum() + 1);
        row.createCell(0).setCellValue(goldLabelEntity.getRecordId());
        row.createCell(row.getLastCellNum()).setCellValue(goldLabelEntity.getPatientId());
        row.createCell(row.getLastCellNum()).setCellValue(goldLabelEntity.getContext());
        return row;
    }

    private HashMap<String,String> dobidui(JSONArray entity, GoldLabelEntity goldLabelEntity, int count) {
        Map<Float,HashMap<String,String>> resultMap = new HashMap<>();
        // TODO: 18/9/5  找出实体数据中得分更高的
        for (int j = 0; j < entity.size(); j++) {
            HashMap<String,String> data = new HashMap<>();
            int errorSize = 0;
            JSONObject typeEntity = entity.getJSONObject(j);

            //TODO 由于结构化key和表头不一致  将结构化的实体映射成goldLavel 进行比对


            for (Map.Entry<String,String> m : goldLabelEntity.getList().entrySet()){
                if (typeEntity.getString(m.getKey()) == null && (m.getValue().equals("") || m.getValue() == null)){

                }else if (typeEntity.getString(m.getKey()) == null || !typeEntity.getString(m.getKey()).equals(m.getValue())){
                    errorSize++;
                }
                data.put(m.getKey(),typeEntity.getString(m.getKey()));
            }
            float score = (1-(float)Math.abs(count-errorSize)/count)*100;
            // TODO: 18/9/5 可能会有得分相同的两条实体，后一条会覆盖前一条
            resultMap.put(score,data);
        }
        Optional<Map.Entry<Float,HashMap<String,String>>> resultOptional = resultMap.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).findFirst();
        if (resultOptional.isPresent()){
            HashMap<String,String> result = resultOptional.get().getValue();
            result.put("比对得分",resultOptional.get().getKey().toString());
            return result;
        }
        else{
            HashMap<String,String> result = new HashMap<>();
            result.put("比对得分",0+"");
            return result;
        }
    }
}
