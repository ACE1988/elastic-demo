package com.example.elastic.service.elastic;

import com.example.elastic.entity.JJRCustomers;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerElasticRepository extends ElasticsearchRepository<JJRCustomers,String> {

    List<JJRCustomers> queryAllByUserIdBefore(long userId);


    List<JJRCustomers> findJJRCustomersByAgentIdEquals(String agentId, Pageable pageable);

   JJRCustomers queryAllByUserIdAndAgentId(@Param("userId") long userId,@Param("agentId") String agentId);

}
