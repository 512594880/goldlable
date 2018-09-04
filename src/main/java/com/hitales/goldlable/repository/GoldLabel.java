package com.hitales.goldlable.repository;

import com.hitales.goldlable.Entity.GoldLabelEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wangxi on 18/9/4.
 */
public interface GoldLabel extends MongoRepository<GoldLabelEntity,String> {
}
