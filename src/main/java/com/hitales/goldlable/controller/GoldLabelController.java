package com.hitales.goldlable.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
