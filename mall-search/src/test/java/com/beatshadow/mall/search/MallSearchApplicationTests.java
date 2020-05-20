package com.beatshadow.mall.search;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@Slf4j
@SpringBootTest
class MallSearchApplicationTests {

    //https://www.elastic.co/guide/index.html
    //https://www.elastic.co/guide/en/elasticsearch/client/index.html
    //https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/index.html
    //https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html
    @Autowired
    private RestHighLevelClient restHighLevelClient ;

    @Test
    void contextLoads() {
    }

    @Test
    void testIndex() throws IOException {
        IndexRequest createIndexRequest = new IndexRequest("test");
        createIndexRequest.source("1");
        RequestOptions requestOptions = RequestOptions.DEFAULT;
        IndexResponse index = restHighLevelClient.index(createIndexRequest, requestOptions);
        System.out.println(index);

    }

}

@Builder
@Data
class Person{
    private int id ;
    private String name ;
}
