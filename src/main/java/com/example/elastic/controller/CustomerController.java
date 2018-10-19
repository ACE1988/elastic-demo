package com.example.elastic.controller;

import com.example.elastic.Util.DateUtils;
import com.example.elastic.entity.JJRCustomers;
import com.example.elastic.service.elastic.CustomerElasticRepository;
import com.example.elastic.service.mybatic.CustomerRepository;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("customer")
public class CustomerController {

    Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

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

        //elasticRepository
        //1 根据id查询
        Optional<JJRCustomers> cc  = customerElasticRepository.findById("1483319acbbb4ec6849350b267daf358");
        //根据userId 查询
        JJRCustomers jjrCustomers = customerElasticRepository.queryByUserId(10000027L);

        QueryBuilder builder = QueryBuilders.matchPhraseQuery("agentId",agentId);
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.ASC,"userId");
        Iterable<JJRCustomers> c = customerElasticRepository.search(builder,pageable);
        List<JJRCustomers> customers = customerElasticRepository.findJJRCustomersByAgentIdEquals(agentId,pageable);

        //查询 elasticTemplate
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .withPageable(pageable)
                .build();
        List<String> ids = elasticsearchTemplate.queryForIds(query);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIds(ids).build();
        List<JJRCustomers> jjrCustomersList =  elasticsearchTemplate.queryForList(searchQuery,JJRCustomers.class);
        LOGGER.info("customer size ={} ",jjrCustomersList.size());

        return "boolean";
    }

    @RequestMapping("elastic/save")
    public String saveCustomerElastic(@RequestParam("agentId") String agentId){

        List<JJRCustomers> customers = customerRepository.queryAllByAgentId(agentId);
        if(customers.size() > 0){
            customerElasticRepository.saveAll(customers);
        }
        return "";
    }


    @RequestMapping("elastic/query")
    public String queryCustomerElastic(@RequestParam("agentId") String agentId,
                                       @RequestParam(value = "pageNo",required = false,defaultValue = "0") Integer pageNo,
                                       @RequestParam(value = "pageSize",required = false,defaultValue = "100") Integer pageSize){
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("agentId",agentId);
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.Direction.ASC,"userId");
        Page<JJRCustomers> page  = customerElasticRepository.search(termQueryBuilder,pageable);
        LOGGER.info("total ={}",page.getTotalElements());
        return JSONObject.valueToString(page.getContent());

    }



    @RequestMapping("/update_template")
    public String updateAgentId(@RequestParam("agentId") String agenId,@RequestParam("userId") long userId){
        //根据userId查询
        JJRCustomers jjrCustomers = customerElasticRepository.queryByUserId(userId);

        //修改
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.setClazz(JJRCustomers.class);
        updateQuery.setId(jjrCustomers.getId());
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.doc(Requests.INDEX_CONTENT_TYPE,"agentId",agenId,"updateTime",new Date().getTime());

        updateQuery.setUpdateRequest(updateRequest);
        //updateQuery.setType("customer");
        //updateQuery.setIndexName("lxgs");

        UpdateResponse response = elasticsearchTemplate.update(updateQuery);
        response.getShardInfo().getSuccessful();
        return "success";
    }

    @RequestMapping("update_repository")
    public String updateRepository(@RequestParam("id") String id,@RequestParam("agentId") String agentId){
        Optional<JJRCustomers> jjrCustomers = customerElasticRepository.findById(id);
        if(jjrCustomers.isPresent()){
            JJRCustomers customer = jjrCustomers.get();
            customer.setAgentId(agentId);
            customer.setUpdateTime(new Date());
            customer = customerElasticRepository.save(customer);
            LOGGER.info("customer agentId={}",customer.getAgentId());
        }
        return "update repository" ;
    }

    //TODO 聚合查询
    @RequestMapping("aggregation")
    public String aggregation(){
        TermsAggregationBuilder teamAgent= AggregationBuilders.terms("agg_agent ").field("agentId");
        Date date = DateUtils.addDayToDate(new Date(),-20);
        LOGGER.info("date={}",DateUtils.convert(date,DateUtils.DATE_TIME_FORMAT));
        //时间范围
        QueryBuilder filterBuilder = QueryBuilders.rangeQuery("registerTime").gt(date.getTime());

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(filterBuilder)
                .addAggregation(teamAgent)
                .build();
//        query.getQuery();
        Page<JJRCustomers> page = customerElasticRepository.search(query);
        return "aggregation";
    }

    //对查询结果进行过滤
    @RequestMapping("filter_query")
    public String filterQuery(@RequestParam("agentId") String agentId){
        //过滤指定字段指定值
//        QueryBuilder filterBuilder = QueryBuilders.termsQuery("userId","10000103","10000126","10000244","10000016");
        //指定查询字段取值范围
//        QueryBuilder filterBuilder = QueryBuilders.rangeQuery("userId").gt("10000103").lt("10000254");
        //小于等于
//        QueryBuilder filterBuilder = QueryBuilders.rangeQuery("userId").lte(10000254L);
        //大于等于
//        QueryBuilder filterBuilder = QueryBuilders.rangeQuery("userId").gte(10000254L);

        Date date = DateUtils.addDayToDate(new Date(),-20);
        LOGGER.info("date={}",DateUtils.convert(date,DateUtils.DATE_TIME_FORMAT));
        //时间范围
        QueryBuilder filterBuilder = QueryBuilders.rangeQuery("registerTime").gt(date.getTime());

        QueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("agentId",agentId);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(0,10000, Sort.Direction.DESC,"userId"))
//                .withSort(SortBuilders.fieldSort("userId"))
                .withFilter(filterBuilder)
                .build();

        Page<JJRCustomers> page = customerElasticRepository.search(searchQuery);
        List<JJRCustomers> list = page.getContent();
        LOGGER.info("{},{}",DateUtils.convert(list.get(0).getRegisterTime(),DateUtils.DATE_TIME_FORMAT),
                DateUtils.convert(list.get(list.size()-1).getRegisterTime(),DateUtils.DATE_TIME_FORMAT));
        LOGGER.info("page total={},size={}",page.getTotalPages(),page.getContent().size());
        return "filter query";
    }
}
