package com.example.elastic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.example.elastic.service.elastic")
@EnableJpaRepositories(basePackages = {"com.example.elastic.service.mybatic"})
public class ElasticDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticDemoApplication.class, args);
    }
}
