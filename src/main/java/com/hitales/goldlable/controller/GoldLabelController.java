package com.hitales.goldlable.controller;

import com.hitales.goldlable.Entity.GoldLabelEntity;
import com.hitales.goldlable.Entity.JSONResult;
import com.hitales.goldlable.Tools.ResultUtil;
import com.hitales.goldlable.repository.GoldLabelRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by wangxi on 18/9/4.
 */
@Api(value = "GoldLabelController",description = "GoldLabelController")
@RestController
@RequestMapping("/")
public class GoldLabelController {


    @Autowired
    private GoldLabelRepository goldLabelRepository;

    @PostMapping(value = "test")
    public String test(){
            return "test";
    }

    /**
     * 上传文件即可以单个也可以多个同时上传
     * @param
     * @return
     */
    @ApiOperation(value = "上传金标文件",notes = "上传excel各实体金标文件")
    @PostMapping(value = "upload")
    @ResponseBody
    public JSONResult upload(@RequestParam("file") MultipartFile file){
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


}
