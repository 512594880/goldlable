package com.hitales.goldlable.DbProperties;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxi on 18/9/10.
 */
@Configuration
public class MultipleMongoConfig {

    @Autowired
    private MultipleMongoProperties mongoProperties;


    @Primary
    @Bean
    @Qualifier(PrimaryMongoConfig.MONGO_TEMPLATE)
    public MongoTemplate primaryMongoTemplate(){
        return new MongoTemplate(primaryFactory(this.mongoProperties.getPrimary()));
    }


    @Bean
    @Qualifier(SecondaryMongoConfig.MONGO_TEMPLATE)
    public MongoTemplate secondaryMongoTemplate(){
        return new MongoTemplate(secondaryFactory(this.mongoProperties.getSecondary()));
    }


    //使用认证 正式数据库一般都用这个
//    @Bean
//    @Primary
//    public MongoDbFactory primaryFactory(MongoProperties mongoProperties){
//        ServerAddress serverAddress = new ServerAddress(mongoProperties.getHost(),mongoProperties.getPort());
//        List<MongoCredential> mongoCredentialList = new ArrayList<>();
//        mongoCredentialList.add(MongoCredential.createCredential(mongoProperties.getUsername(),mongoProperties.getDatabase(),mongoProperties.getPassword()));
//
//        return new SimpleMongoDbFactory(new MongoClient(serverAddress,mongoCredentialList),mongoProperties.getDatabase());
//    }

    @Bean
    @Primary
    public MongoDbFactory primaryFactory(MongoProperties mongoProperties){
        return new SimpleMongoDbFactory(new MongoClient(mongoProperties.getHost(),mongoProperties.getPort()),mongoProperties.getDatabase());
    }

    @Bean
    public MongoDbFactory secondaryFactory(MongoProperties mongoProperties){
        ServerAddress serverAddress = new ServerAddress(mongoProperties.getHost(),mongoProperties.getPort());
        List<MongoCredential> mongoCredentialList = new ArrayList<>();
        mongoCredentialList.add(MongoCredential.createCredential(mongoProperties.getUsername(),mongoProperties.getDatabase(),mongoProperties.getPassword()));

        return new SimpleMongoDbFactory(new MongoClient(serverAddress,mongoCredentialList),mongoProperties.getDatabase());
    }
}
