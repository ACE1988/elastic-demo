package com.example.elastic.controller;

import com.example.elastic.entity.CustomerComment;
import com.example.elastic.service.elastic.CustomerCommentElasticRepository;
import com.example.elastic.service.mybatic.CustomerCommentRepository;
import com.mongodb.util.JSON;
import org.elasticsearch.index.query.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
    public String queryCustomerCommentByComment(@RequestParam("comment") String comment,
                                                @RequestParam(value = "operatorName",required = false,defaultValue = "agentId") String operatorName){
        MatchPhraseQueryBuilder builder1 = QueryBuilders.matchPhraseQuery("comment",comment);
        builder1.analyzer("standard");
        MatchPhraseQueryBuilder builder2 = QueryBuilders.matchPhraseQuery("operatorName",operatorName);
        builder2.analyzer("standard");
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(builder1).must(builder2);

        LOGGER.info("match phrase query dsl={}",queryBuilder.toString());
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.DESC,"createTime");
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
                .build();
        Page<CustomerComment> commentPage = customerCommentElasticRepository.search(searchQuery);
        List<CustomerComment> list = commentPage.getContent();
        LOGGER.info("size={}",list.size());
        return JSONObject.valueToString(list);
    }


    @RequestMapping("elastic/term")
    public String queryCommentTermQuery(@RequestParam("field") String field ,
                                        @RequestParam("value") String value,
                                        @RequestParam(value = "pageSize",defaultValue = "100") Integer pageSize,
                                        @RequestParam(value = "pageNo",defaultValue = "0") Integer pageNo){

        String [] values = value.split(",");

        TermsQueryBuilder builder = QueryBuilders.termsQuery(field,values);
        //非评分模式
        ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(builder);
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.Direction.ASC,"createTime");
        LOGGER.info("dsl={}",constantScoreQueryBuilder.toString());
        Page<CustomerComment> commentPage =  customerCommentElasticRepository.search(constantScoreQueryBuilder,pageable);
        List<CustomerComment> list = commentPage.getContent();
        LOGGER.info("size={}",list.size());
        return JSONObject.valueToString(list);
    }

    @RequestMapping("elastic/commonTerm")
    public String queryCommentCommonTermQuery(@RequestParam("field") String field ,
                                        @RequestParam("value") String value,
                                        @RequestParam(value = "pageSize",defaultValue = "100") Integer pageSize,
                                        @RequestParam(value = "pageNo",defaultValue = "0") Integer pageNo){

        String [] values = value.split(",");
        CommonTermsQueryBuilder builder = QueryBuilders.commonTermsQuery(field,values);
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.Direction.ASC,"createTime");
        LOGGER.info("dsl={}",builder.toString());
        Page<CustomerComment> commentPage =  customerCommentElasticRepository.search(builder,pageable);
        List<CustomerComment> list = commentPage.getContent();
        LOGGER.info("size={}",list.size());
        return JSONObject.valueToString(list);
    }

    @RequestMapping("elastic/multi")
    public String queryCommentMultiMatch(@RequestParam("field1") String field1 ,
                                         @RequestParam("field2") String field2 ,
                                         @RequestParam("value") String value,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "100") Integer pageSize,
                                         @RequestParam(value = "pageNo",required = false,defaultValue = "0") Integer pageNo){

        MultiMatchQueryBuilder builder =  QueryBuilders.multiMatchQuery(value,field1,field2);
        builder.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
        builder.type(MultiMatchQueryBuilder.Type.CROSS_FIELDS);
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.Direction.DESC,"createTime");
        Page<CustomerComment> commentPage = customerCommentElasticRepository.search(builder,pageable);
        List<CustomerComment> list = commentPage.getContent();
        LOGGER.info("size={}",list.size());
        return JSONObject.valueToString(list);

    }

    @RequestMapping("elastic/matchPhrase")
    public String queryCommentMatch(@RequestParam(value = "field" )String field ,
                                    @RequestParam("comment") String comment,
                                    @RequestParam("slop") Integer slop,
                                    @RequestParam(value = "pageSize",required = false,defaultValue = "100") Integer pageSize,
                                    @RequestParam(value = "pageNo",required = false,defaultValue = "0") Integer pageNo){
        MatchPhraseQueryBuilder builder =  QueryBuilders.matchPhraseQuery(field,comment);
        builder.slop(slop);
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.Direction.DESC,"createTime");
        Page<CustomerComment> commentPage = customerCommentElasticRepository.search(builder,pageable);
        List<CustomerComment> list = commentPage.getContent();
        LOGGER.info("size={}",list.size());
        return JSONObject.valueToString(list);
    }

    @RequestMapping("elastic/prefixQuery")
    public String matchPhrasePrefixQuery(@RequestParam(value = "field" )String field ,
                                         @RequestParam("value") String value,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "100") Integer pageSize,
                                         @RequestParam(value = "pageNo",required = false,defaultValue = "0") Integer pageNo){

        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.Direction.DESC,"createTime");
        MatchPhrasePrefixQueryBuilder builder = QueryBuilders.matchPhrasePrefixQuery(field,value);
        LOGGER.info("match phrase prefix dsl={}",builder.toString());
        customerCommentElasticRepository.search(builder,pageable);
        Page<CustomerComment> commentPage = customerCommentElasticRepository.search(builder,pageable);
        List<CustomerComment> list = commentPage.getContent();
        LOGGER.info("size={}",list.size());
        return JSONObject.valueToString(list);
    }

    @RequestMapping("elastic/filter")
    public String filterQuery(@RequestParam(value = "field") String field,
                              @RequestParam(value = "value") String value,
                              @RequestParam(value = "field2") String field2,
                              @RequestParam(value = "value2") String value2,
                              @RequestParam(value = "field3") String field3,
                              @RequestParam(value = "value3") String value3){
        MatchPhraseQueryBuilder builder1 =  QueryBuilders.matchPhraseQuery(field,value);
        MatchPhraseQueryBuilder builder2 =  QueryBuilders.matchPhraseQuery(field2,value2);
        MatchPhraseQueryBuilder builder3 = QueryBuilders.matchPhraseQuery(field3,value3);
        BoolQueryBuilder queryBuilder2 = QueryBuilders.boolQuery().must(builder3);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(builder1).should(builder2).must(queryBuilder2);
        ConstantScoreQueryBuilder constantScoreQueryBuilder = QueryBuilders.constantScoreQuery(queryBuilder);
        LOGGER.info("filter query dsl ={}",constantScoreQueryBuilder.toString());
        Iterable<CustomerComment> list = customerCommentElasticRepository.search(constantScoreQueryBuilder);
        List<CustomerComment> result = new ArrayList<>();
        list.forEach(item -> {
            result.add(item);
        });
        return JSONObject.valueToString(result);
    }

    @RequestMapping("elastic/terms")
    public String termsQuery(@RequestParam(value = "field") String field,
                             @RequestParam(value = "value2",required = false) String value2,
                             @RequestParam(value = "value1") String value1){
        TermsQueryBuilder builder =  QueryBuilders.termsQuery(field,value1,value2);
        ConstantScoreQueryBuilder queryBuilder = QueryBuilders.constantScoreQuery(builder);
        LOGGER.info("terms dsl ={}",queryBuilder.toString());
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.ASC,field);
        Page<CustomerComment> page = customerCommentElasticRepository.search(queryBuilder,pageable);
        return JSONObject.valueToString(page.getContent());
    }

    @RequestMapping("elastic/between")
    public String betweenQuery(@RequestParam(value = "field") String field,
                               @RequestParam(value = "value2",required = false) String value2,
                               @RequestParam(value = "value1") String value1 ){
        RangeQueryBuilder builder =  QueryBuilders.rangeQuery(field).gte(value1).lte(value2);
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.ASC,field);
        LOGGER.info("terms dsl ={}",builder.toString());
        Page<CustomerComment> page = customerCommentElasticRepository.search(builder,pageable);
        return JSONObject.valueToString(page.getContent());
    }

    @RequestMapping("elastic/exists")
    public String existsQuery(@RequestParam("field") String field){
        ExistsQueryBuilder existsQueryBuilder =  QueryBuilders.existsQuery(field);
        ConstantScoreQueryBuilder queryBuilder = QueryBuilders.constantScoreQuery(existsQueryBuilder);
        LOGGER.info("terms dsl ={}",queryBuilder.toString());
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.ASC,field);
        Page<CustomerComment> page = customerCommentElasticRepository.search(queryBuilder,pageable);
        return JSONObject.valueToString(page.getContent());
    }

}
