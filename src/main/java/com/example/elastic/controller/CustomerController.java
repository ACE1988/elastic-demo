package com.example.elastic.controller;

import com.example.elastic.entity.JJRCustomers;
import com.example.elastic.service.elastic.CustomerElasticRepository;
import com.example.elastic.service.mybatic.CustomerRepository;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository ;

    @Autowired
    CustomerElasticRepository customerElasticRepository ;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;


    @GetMapping("/save")
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
        Optional<JJRCustomers> cc  = customerElasticRepository.findById("1483319acbbb4ec6849350b267daf358");
        QueryBuilder builder = QueryBuilders.matchPhraseQuery("agentId",agentId);
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.ASC,"userId");
//        Iterable<JJRCustomers> c = customerElasticRepository.search(builder,pageable);
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .withPageable(pageable)
                .build();
        List<String> ids = elasticsearchTemplate.queryForIds(query);

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIds(ids).build();
        List<JJRCustomers> jjrCustomers =  elasticsearchTemplate.queryForList(searchQuery,JJRCustomers.class);

        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setClazz(JJRCustomers.class);
        updateQuery.setId("1483319acbbb4ec6849350b267daf358");
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.doc(Requests.INDEX_CONTENT_TYPE,"agentId","010101049","agentName","彤姐");

        updateQuery.setUpdateRequest(updateRequest);
        updateQuery.setType("customer");
        updateQuery.setIndexName("lxgs");

        UpdateResponse response = elasticsearchTemplate.update(updateQuery);

//        List<JJRCustomers> customers = customerElasticRepository.findJJRCustomersByAgentIdEquals(agentId,pageable);
        return "boolean";
    }

    @RequestMapping("/update")
    public String updateAgentId(@RequestParam("agentId") String agenId,@RequestParam("userId") long userId){
        int update = customerRepository.updateByUserIdAndAgentId(userId,agenId);
        return "success";
    }
}
