package com.hitales.goldlable.controller;

import com.hitales.goldlable.Entity.GoldLabelEntity;
import com.hitales.goldlable.repository.GoldLabel;
import io.swagger.annotations.Api;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by wangxi on 18/9/4.
 */
@Api(value = "GoldLabelController",description = "GoldLabelController")
@RestController
@RequestMapping("/")
public class GoldLabelController {

    @PostMapping(value = "test")
    public String test(){
            return "你是傻逼";
    }

    @Autowired
    private GoldLabel goldLabel;


    @ResponseBody
    public String uploadOrigin(HttpServletRequest request, @PathVariable("type") String type){
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        try {
            for (MultipartFile file : files) {
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
                        for (int j = 0; j < row.getLastCellNum(); j++) {
                            Cell cell = row.getCell(i);
                            if (cell == null)continue;
                            map.put(head.get(j),cell.toString());
                        }
                        GoldLabelEntity goldLabelEntity = new GoldLabelEntity();
                        goldLabelEntity.setType(type);
                        goldLabelEntity.setContext(map.get("上下文"));
                        goldLabelEntity.setRecordId(map.get("病历（RID）"));
                        goldLabelEntity.setPatientId(map.get("患者（PID）"));
                        map.remove("上下文");
                        map.remove("病历（RID）");
                        map.remove("患者（PID）");
                        goldLabelEntity.setList(map);
                        goldLabel.save(goldLabelEntity);
                    }
                }else return file.getOriginalFilename() + "有问题";
            }

        }catch (IOException e){
            e.printStackTrace();
            return "IOException";
        }



        return "";
    }

}
