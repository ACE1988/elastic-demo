package com.example.elastic.controller;

import com.example.elastic.Util.DateUtils;
import com.example.elastic.entity.CustomerNote;
import com.example.elastic.service.elastic.CustomerNoteElasticRepository;
import com.example.elastic.service.mybatic.CustomerNoteRepository;
import com.mongodb.util.JSON;
import org.apache.lucene.search.BooleanQuery;
import org.elasticsearch.index.query.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("note")
public class CustomerNoteController {

    @Autowired
    CustomerNoteRepository customerNoteRepository;

    @Autowired
    CustomerNoteElasticRepository customerNoteElasticRepository ;

    Logger LOGGER = LoggerFactory.getLogger(CustomerNoteController.class);

    @RequestMapping("query")
    public void query(@RequestParam("userId") Long userId,@RequestParam("userId2") long userId2){

        List<CustomerNote> list = customerNoteRepository.findAllByUserIdBetween(userId,userId2);
        LOGGER.info("list size={}",list.size());
        if(list.size() > 0){
            customerNoteElasticRepository.saveAll(list);
        }
    }

    @RequestMapping("elastic/query")
    public String  queryCustomerNote(@RequestParam("operatorId") String operatorId,
                                     @RequestParam("createTime") String createTime){

        Date date = DateUtils.convert(createTime);
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime").gt(date.getTime());
        TermQueryBuilder termQueryBuilder =  QueryBuilders.termQuery("operatorId",operatorId);
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery().must(rangeQueryBuilder).must(termQueryBuilder);
        ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(booleanQuery);
        LOGGER.info("customer note dsl = {}",constantScoreQueryBuilder.toString());
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.DESC,"createTime");
        Page<CustomerNote> page = customerNoteElasticRepository.search(constantScoreQueryBuilder,pageable);
        return JSONObject.valueToString(page.getContent());
    }
}
