package com.hitales.goldlable.controller;

import com.hitales.goldlable.Entity.JSONResult;
import com.hitales.goldlable.service.GoldLabelCompareService;
import com.hitales.goldlable.service.GoldLableReadService;
import com.hitales.goldlable.service.StructService;
import com.hitales.goldlable.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by wangxi on 18/9/4.
 */
@Api(value = "GoldLabelController",description = "GoldLabelController")
@RestController
@RequestMapping("/")
@Slf4j
public class GoldLabelController {

    @Autowired
    private GoldLableReadService goldLableReadService;
    @Autowired
    private GoldLabelCompareService goldLabelCompareService;
    @Autowired
    private TestService testService;
    @Autowired
    private StructService structService;


    @PostMapping(value = "test")
    public Object test(@RequestBody String str) throws ExecutionException, InterruptedException {
        testService.doCompare("你好啊" + str);

        return str;
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
        return goldLableReadService.readExcel(file);
    }

    @ApiOperation(value = "结构化金表比对",notes = "对对应类型的数据进行结构化以及金标比对")
    @PostMapping(value = "goldLabelCompare")
    @ResponseBody
    public void goldLabelCompare(@RequestBody ArrayList<String> types){
        goldLabelCompareService.compare(types);
    }

}
