package com.beatshadow.mall.thirdparty.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author gnehcgnaw
 * @since
 */
@Configuration
public class MongoConfig {
    public @Bean
    MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    public @Bean
    MongoTemplate mongoTemplate(){
        return new MongoTemplate(mongoClient(),"mall-third-party");
    }
}
