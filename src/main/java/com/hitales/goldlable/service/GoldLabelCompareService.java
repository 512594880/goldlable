package com.hitales.goldlable.service;

import com.hitales.goldlable.Entity.GoldLabelEntity;
import com.hitales.goldlable.Tools.Constant;
import com.hitales.goldlable.repository.GoldLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangxi on 18/9/5.
 */
@Service
public class GoldLabelCompareService {

    @Autowired
    private GoldLabelRepository goldLabelRepository;

    @Autowired
    private StructService structService;

    public void compare(List<String> types){
        for (String type:types) {
            switch (type){
                case Constant.TYPE_DRUG:
                    drugComparre(type);
                    break;
                case Constant.TYPE_DIAG:

                    break;
                case Constant.TYPE_SYMPTOM:

                    break;
                case Constant.TYPE_TEST:

                    break;

            }
        }
    }


    private void drugComparre(String type){
        List<GoldLabelEntity> goldLabelEntityList = goldLabelRepository.findByType(type);
        for (int i = 0; i < goldLabelEntityList.size(); i++) {
            String context = goldLabelEntityList.get(i).getContext();
            //调用结构化


        }
    }








}
