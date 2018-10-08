package com.example.elastic.service.elastic;

import com.example.elastic.entity.JJRCustomers;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomerElasticRepository extends ElasticsearchRepository<JJRCustomers,String> {
}
