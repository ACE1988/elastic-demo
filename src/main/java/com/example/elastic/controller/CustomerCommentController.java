package com.example.elastic.controller;

import com.example.elastic.entity.CustomerComment;
import com.example.elastic.service.elastic.CustomerCommentElasticRepository;
import com.example.elastic.service.mybatic.CustomerCommentRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CustomerCommentController  {

    Logger LOGGER = LoggerFactory.getLogger(CustomerCommentController.class);

    @Autowired
    CustomerCommentRepository customerCommentRepository ;

    @Autowired
    CustomerCommentElasticRepository customerCommentElasticRepository;


    @RequestMapping("query")
    public String queryByUserId(@RequestParam("userId") long userId){
        List<CustomerComment> list = customerCommentRepository.findAllByUserId(userId);
        LOGGER.info("userId={}",list.size());
        return "ccc";
    }


    @RequestMapping("elastic")
    public String saveCustomerCommentElastic(@RequestParam("startUserId") long startUserId,
                                             @RequestParam("endUserId") long endUserId){

        List<CustomerComment> list = customerCommentRepository.findAllByUserIdBetween(startUserId,endUserId);
        LOGGER.info("size={}",list.size());
        if(list.size() > 0){
            customerCommentElasticRepository.saveAll(list);
        }
        return "save elastic" ;
    }

    @RequestMapping("elastic/query")
    public String queryCustomerCommentByUserId(@RequestParam("userId") long userId){

        List<CustomerComment> list = customerCommentElasticRepository.
                findAllByUserId(userId, Sort.by(Sort.Direction.DESC,"createTime"));
        LOGGER.info("size={}",list.size());
        return "query" ;
    }

    @RequestMapping("elastic/comments")
    public String queryCustomerCommentByComment(@RequestParam("comment") String comment){
        QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("comment",comment);
//        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(comment,"comment,userName,operatorName");
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.DESC,"createTime");
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                //.withSort(SortBuilders.fieldSort("createTime"))
                .build();
        Page<CustomerComment> commentPage = customerCommentElasticRepository.search(searchQuery);
        List<CustomerComment> list = commentPage.getContent();
        LOGGER.info("size={}",list.size());
        return JSONObject.valueToString(list);
    }
}
