package com.hitales.goldlable.secondRepository;

import com.hitales.goldlable.Entity.StructStatusResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

/**
 * Created by wangxi on 18/9/10.
 */
public interface StructResultRepository extends MongoRepository<StructStatusResult,String>{
    ArrayList<StructStatusResult> findByBoxId(String boxId);
    ArrayList<StructStatusResult> findByBoxIdAndResult(String boxId,String result);
}
