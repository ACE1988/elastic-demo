package com.example.elastic.controller;

import com.example.elastic.Util.DateUtils;
import com.example.elastic.entity.AgentWechatMessage;
import com.example.elastic.service.elastic.AgentWechatMessageElasticRepository;
import com.example.elastic.service.mybatic.AgentWechatMessageRepository;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  ------------------
 * 2018/10/9    刘节                 Created
 */
@RestController
@RequestMapping("wechat")
public class WechatMessageController {

    @Autowired
    AgentWechatMessageRepository agentWechatMessageRepository;

    @Autowired
    AgentWechatMessageElasticRepository agentWechatMessageElasticRepository ;

    Logger LOGGER = LoggerFactory.getLogger(WechatMessageController.class);

    @GetMapping("/save")
    public String save(Integer start, Integer end){
        Date startTime = DateUtils.getStartDatetime(new Date(),-start);
        Date endTime = DateUtils.getEndDatetime(new Date(),-end);

        List<AgentWechatMessage> list = agentWechatMessageRepository.
                findAgentWechatMessageBySendTimeBetweenOrderBySendTime(startTime,endTime);
        LOGGER.info("【微信聊天记录】 size={}",list.size());

        list.stream().forEach(item -> {
            agentWechatMessageElasticRepository.save(item);
        });
        return "wechat" ;
    }


    @GetMapping("query")
    public String query(){
//        QueryBuilder builder = QueryBuilders.matchQuery("content","面试");
//        MultiMatchQueryBuilder builder1 = QueryBuilders.multiMatchQuery("面试通过","content");
        MatchPhraseQueryBuilder builder1 = QueryBuilders.matchPhraseQuery("content","面9090990试");
        MatchPhraseQueryBuilder builder2 = QueryBuilders.matchPhraseQuery("agentName","丽姐");
        QueryBuilder qb = QueryBuilders.boolQuery().should(builder1).must(builder2);
        Pageable pageable = PageRequest.of(0,100, Sort.Direction.ASC,"createTime");
        Iterable<AgentWechatMessage>  messages =  agentWechatMessageElasticRepository.search(qb,pageable);
        return "query wechat" ;
    }
}