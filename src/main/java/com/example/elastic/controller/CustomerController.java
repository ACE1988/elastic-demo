package com.example.elastic.controller;

import com.example.elastic.entity.JJRCustomers;
import com.example.elastic.service.elastic.CustomerElasticRepository;
import com.example.elastic.service.mybatic.CustomerRepository;
import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository ;

    @Autowired
    CustomerElasticRepository customerElasticRepository ;

    @GetMapping("/customer")
    public String queryCustomer(long startUserId,long endUserId){
        List<JJRCustomers> list = customerRepository.queryAllByUserIdBetween(startUserId,endUserId);
        list.stream().forEach(item -> {
            if(item != null && item.getUserId() > 0){
                customerElasticRepository.save(item);
            }
        });
        return "success";
    }

    @GetMapping("/query")
    public String query(String agentId){
//        Optional<JJRCustomers> cc  = customerElasticRepository.findById("1483319acbbb4ec6849350b267daf358");
        QueryBuilder builder = QueryBuilders.matchPhraseQuery("agentId",agentId);
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.ASC,"userId");
        Iterable<JJRCustomers> c = customerElasticRepository.search(builder,pageable);

        List<JJRCustomers> customers = customerElasticRepository.findJJRCustomersByAgentIdEquals(agentId,pageable);
        return "boolean";
    }
}
