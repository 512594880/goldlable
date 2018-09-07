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

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
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
    public ArrayList<String> goldLabelCompare(@RequestBody ArrayList<String> types){
        ArrayList<String> files = new ArrayList<>();
        goldLabelCompareService.compare(types);
        for (String type:types) {
            String host = null;
            try {
                host = InetAddress.getLocalHost().getHostAddress();
                String url = "http://" + host + ":8888/download?type=" + type;
                files.add(url);
            } catch (UnknownHostException e) {
                log.error("get server host Exception e:", e);
            }

        }
        return files;
    }

    /**
     * 下载文件
     * @param res
     */
    @GetMapping(value = "download")
    public void Download(HttpServletResponse res,@RequestParam("type") String type) throws UnsupportedEncodingException {
        String path = "./OriginExcel/"+type+".xlsx";
        String fileName = path.replaceAll(".*/","");
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;

        File downLoadFile = new File(path);
        if (!downLoadFile.exists()){
            try {
                downLoadFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(downLoadFile));
            int nRead;
            while ((nRead = bis.read(buff, 0, buff.length)) != -1) {
                os.write(buff, 0, nRead);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("success");
    }

}
