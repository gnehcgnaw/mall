package com.beatshadow.mall.thirdparty.mongo;

import com.alibaba.fastjson.JSON;
import com.beatshadow.mall.thirdparty.entity.mongo.Person;
import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ResourceUtils;

import java.io.*;

@SpringBootTest
public class MongoTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test() {
        //获取当前连接的数据库的名字，如果没有配置，显示的是test
        System.out.println(mongoTemplate.getDb().getName());
        mongoTemplate.insert(new Person("beatshadow", 29));
        System.out.println(mongoTemplate.findOne(new Query(Criteria.where("name").is("beatshadow")), Person.class));

    }

    @Test
    public void test2() throws IOException {
        File file = ResourceUtils.getFile("classpath:mongo");
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String data;
        StringBuilder stringBuffer = new StringBuilder();
        while ((data = br.readLine()) != null) {
            stringBuffer.append(data);
        }
        data = stringBuffer.toString();
        mongoTemplate.insert(Document.parse(data));
        br.close();
        isr.close();
        fis.close();
    }
}