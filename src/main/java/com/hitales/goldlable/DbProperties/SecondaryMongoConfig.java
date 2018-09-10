package com.hitales.goldlable.DbProperties;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by wangxi on 18/9/10.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.hitales.goldlable.secondRepository", mongoTemplateRef = SecondaryMongoConfig.MONGO_TEMPLATE)
public class SecondaryMongoConfig {
    protected static final String MONGO_TEMPLATE = "secondaryMongoTemplate";
}
