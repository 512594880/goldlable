package com.hitales.goldlable.repository;

import com.hitales.goldlable.Entity.GoldLabelEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by wangxi on 18/9/4.
 */
public interface GoldLabelRepository extends MongoRepository<GoldLabelEntity,String> {
    public List<GoldLabelEntity> findByType(String type);
}
