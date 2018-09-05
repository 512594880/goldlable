package com.hitales.goldlable.service;

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

        }
    }
}
