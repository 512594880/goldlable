package com.hitales.goldlable.service;

import com.hitales.goldlable.Entity.GoldLabelEntity;
import com.hitales.goldlable.Entity.JSONResult;
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
import java.util.ArrayList;
import java.util.HashMap;
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
                sheetexcel.getRow(0).forEach(cell -> head.add(cell.toString()));
                HashMap<String,String> map = new HashMap<>();
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
                    goldLabelEntity.setContext(map.get("上下文"));
                    goldLabelEntity.setRecordId(map.get("病历（RID）"));
                    goldLabelEntity.setPatientId(map.get("患者（PID）"));
                    goldLabelEntity.setType(getType(file.getOriginalFilename()));
                    map.remove("上下文");
                    map.remove("病历（RID）");
                    map.remove("患者（PID）");
                    goldLabelEntity.setList(map);
                    goldLabelRepository.save(goldLabelEntity);
                }
            }else return ResultUtil.error(111,"文件有问题");

        }catch (IOException e){
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    public void bidui(String type){
        List<GoldLabelEntity> goldLabelEntityList = goldLabelRepository.findByType(type);
        for (int i = 0; i < goldLabelEntityList.size(); i++) {
            String context = goldLabelEntityList.get(i).getContext();
            //调用结构化



        }
    }





    private String getType(String name){
        if (name.contains("用药"))return "用药";
        else if (name.contains("诊断"))return "诊断";
        else if (name.contains("化验"))return "化验";
        else if (name.contains("症状") || name.contains("体征")) return "症状&体征";
        return "错误类型";
    }
}
