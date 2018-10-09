package com.example.elastic.service.elastic;

import com.example.elastic.entity.JJRCustomers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CustomerElasticRepository extends ElasticsearchRepository<JJRCustomers,String> {

    List<JJRCustomers> queryAllByUserIdBefore(long userId);


    List<JJRCustomers> findJJRCustomersByAgentIdEquals(String agentId, Pageable pageable);
}
