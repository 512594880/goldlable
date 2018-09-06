package com.hitales.goldlable.service;

import com.hitales.goldlable.Entity.GoldLabelEntity;
import com.hitales.goldlable.Entity.JSONResult;
import com.hitales.goldlable.Tools.Constant;
import com.hitales.goldlable.Tools.ResultUtil;
import com.hitales.goldlable.repository.GoldLabelRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class GoldLableReadService {
    @Autowired
    private GoldLabelRepository goldLabelRepository;



    public JSONResult readExcel(MultipartFile file){
        try {
            if (Pattern.compile(".*(xls|xlsx|xlsm)$").matcher(file.getOriginalFilename()).matches()) {
                InputStream inputStream =file.getInputStream();
                XSSFWorkbook hssfWorkbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheetexcel = hssfWorkbook.getSheetAt(0);
                List<String> head = new ArrayList<>();
                sheetexcel.getRow(0).forEach(cell ->{
                    if (!"".equals(cell.toString()))
                        head.add(cell.toString().replaceAll("([\n\t ])",""));
                });
                LinkedHashMap<String,String> map = new LinkedHashMap<>();
                for (int i = 1; i <= sheetexcel.getLastRowNum(); i++) {
                    Row row = sheetexcel.getRow(i);
                    if(row == null)
                        continue;
                    for (int j = 0; j < head.size(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null)map.put(head.get(j),"");
                        else map.put(head.get(j),cell.toString());
                    }
                    GoldLabelEntity goldLabelEntity = new GoldLabelEntity();

                    for (int j = 0; j < Constant.DrugShare.length; j++) {
                        String key = Constant.DrugShare[j];
                        Method method = goldLabelEntity.getClass().getMethod(Constant.DrugShareMethodName[j], String.class);
                        method.invoke(goldLabelEntity,map.get(key));
                        map.remove(key);
                    }



//                    goldLabelEntity.setContext(map.get("上下文"));
//                    goldLabelEntity.setRecordId(map.get("病例（RID）"));
//                    goldLabelEntity.setPatientId(map.get("患者（PID）"));
                    goldLabelEntity.setType(getType(file.getOriginalFilename()));
//                    map.remove("上下文");
//                    map.remove("病例（RID）");
//                    map.remove("患者（PID）");
                    goldLabelEntity.setList(map);
                    goldLabelRepository.save(goldLabelEntity);
                }
            }else return ResultUtil.error(111,"文件有问题");

        }catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    private String getType(String name){
        if (name.contains(Constant.TYPE_DRUG))return Constant.TYPE_DRUG;
        else if (name.contains(Constant.TYPE_DIAG))return Constant.TYPE_DIAG;
        else if (name.contains(Constant.TYPE_TEST))return Constant.TYPE_TEST;
        else if (name.contains("症状") || name.contains("体征")) return Constant.TYPE_SYMPTOM;
        else if (name.contains(Constant.TYPE_FAMILY_HISTORY)) return Constant.TYPE_FAMILY_HISTORY;
        else if (name.contains(Constant.TYPE_MENSTRUAL_HISTORY))return Constant.TYPE_MENSTRUAL_HISTORY;
        return "错误类型";
    }
}
