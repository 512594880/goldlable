package com.hitales.goldlable.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hitales.goldlable.Entity.GoldLabelEntity;
import com.hitales.goldlable.repository.GoldLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangxi on 18/9/5.
 */
@Service
public class GoldLabelCompareService {

    @Value("${structUrl}")
    private String structUrl;

    @Autowired
    private GoldLabelRepository goldLabelRepository;

    public void compare(List<String> types){
        for (String type:types) {
            List<GoldLabelEntity> goldLabelEntityList = goldLabelRepository.findByType(type);
            for (int i = 0; i < goldLabelEntityList.size(); i++) {
                GoldLabelEntity goldLabelEntity = goldLabelEntityList.get(i);
                String context = goldLabelEntity.getContext();
                //调用结构化
                JSONObject data = new JSONObject();


                //获取计分字段数量
                int count = (int)goldLabelEntity.getList().entrySet().stream().filter(entry -> entry.getValue() != null).count();

                //获取msdata
                JSONObject msdata = data.getJSONObject("msdata");
                //获取实体数据
                JSONArray entity = msdata.getJSONArray(type);
                // TODO: 18/9/5  找出实体数据中得分更高的
                for (int j = 0; j < entity.size(); j++) {
                    int errorSize = 0;
                    JSONObject typeEntity = entity.getJSONObject(i);
                    goldLabelEntity.getList().forEach((s, s2) -> {
                        if (!typeEntity.getString(s).equals(s2)){

                        }
                    });
                }



            }

        }
    }
}
