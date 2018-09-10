package com.hitales.goldlable.controller;

import com.hitales.goldlable.Entity.StructStatusResult;
import com.hitales.goldlable.secondRepository.StructResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxi on 18/9/10.
 */
@RestController
@RequestMapping("/struct/")
public class StructStatusResultController {

    @Autowired
    private StructResultRepository structResultRepository;

    @Autowired
    @Qualifier(value = "secondaryMongoTemplate")
    protected MongoTemplate secondaryMongoTemplate;


    @PostMapping("findKeyWithBoxId")
    public ArrayList<StructStatusResult> findTypeRecordId(String boxId, String key){
        return structResultRepository.findByBoxIdAndResult(boxId,key);
    }

    @PostMapping("findKeyWithBoxId2")
    public List<StructStatusResult> findTypeRecordId2(String queryStr){
        Query query = new BasicQuery(queryStr);
        return secondaryMongoTemplate.find(query,StructStatusResult.class);
    }
}
