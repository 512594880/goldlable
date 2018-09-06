package com.hitales.goldlable.Tools;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Created by wangxi on 18/9/4.
 */
@Slf4j
public class Constant {
    public static final String  TYPE_DRUG = "用药";
    public static final String  TYPE_DIAG = "诊断";
    public static final String  TYPE_TEST = "化验";
    public static final String  TYPE_SYMPTOM = "症状&体征";
    public static final String TYPE_FAMILY_HISTORY = "家族史";
    public static final String TYPE_MENSTRUAL_HISTORY = "月经史";
    public static final String  TYPE_ERROR = "错误类型";
    public static final String [] DrugShare = {"医院","科室/病种","患者（PID）","病例（RID）","锚点","上下文","备注"};

    public static final String [] DrugShareMethodName = {"setHospital","setDepartments","setPatientId","setRecordId","setAnchor","setContext","setRemark"};


    //配置实体对应的表头名

    public static final HashMap<String,JSONObject> keyMap = new HashMap<String,JSONObject>(){
        {
            try {
                File file =  ResourceUtils.getFile("classpath:config/keymap.json");
                String jsonStr = FileHelper.ReadDataFromFile(file);
                JSONObject object = JSONObject.parseObject(jsonStr);
                for (String key:object.keySet()) {
                    put(key,object.getJSONObject(key));
                }
                log.info("数据加载完成===>" + object.toJSONString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    };
}
