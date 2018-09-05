package com.hitales.goldlable.controller;

import com.hitales.goldlable.Entity.JSONResult;
import com.hitales.goldlable.Tools.FileHelper;
import com.hitales.goldlable.Tools.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wangxi on 18/9/4.
 */
@Api(value = "GoldLabelController",description = "GoldLabelController")
@RestController
@RequestMapping("/")
public class GoldLabelController {

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
    public JSONResult upload(@RequestParam("file")  MultipartFile file){
        try {
            String targetPath = "./uploadFiles/";
            FileHelper.writeClientDataToPath(file,targetPath);
            return ResultUtil.success();
        }catch (Exception e){
            return ResultUtil.error(201,e.getMessage());
        }
    }

}
