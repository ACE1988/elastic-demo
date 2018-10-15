package com.example.elastic.controller;

import com.example.elastic.Util.DateUtils;
import com.example.elastic.entity.AgentWechatMessage;
import com.example.elastic.service.elastic.AgentWechatMessageElasticRepository;
import com.example.elastic.service.mybatic.AgentWechatMessageRepository;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    @GetMapping("/elastic/query")
    public String query(@RequestParam("content") String content,@RequestParam("agentName")String agentName){
//        QueryBuilder builder = QueryBuilders.matchQuery("content","面试");
//        MultiMatchQueryBuilder builder1 = QueryBuilders.multiMatchQuery("面试通过","content");
        MatchPhraseQueryBuilder builder1 = QueryBuilders.matchPhraseQuery("content",content);
        MatchPhraseQueryBuilder builder2 = QueryBuilders.matchPhraseQuery("agentName",agentName);
        QueryBuilder qb = QueryBuilders.boolQuery().should(builder1).must(builder2);


        Pageable pageable = PageRequest.of(0,100, Sort.Direction.ASC,"createTime");
        Page<AgentWechatMessage>  messages =  agentWechatMessageElasticRepository.search(qb,pageable);
        List<AgentWechatMessage> list = messages.getContent();
        return JSONObject.valueToString(list);
    }

    @RequestMapping("query2")
    public String query2(@RequestParam("agentId") String agentId, @RequestParam("content") String content){
        LOGGER.info("agentId={},content={}",agentId,content);
        Pageable pageable = PageRequest.of(1,100, Sort.Direction.ASC,"createTime");
//        Iterable<AgentWechatMessage>  messages =  agentWechatMessageElasticRepository.findByAgentIdAndContent(agentId,content,pageable);
//        Iterable<AgentWechatMessage>  messages2 = agentWechatMessageElasticRepository.findByAgentIdOrContent(agentId,content,pageable);

//        List<AgentWechatMessage> list1 = agentWechatMessageElasticRepository.findByAgentIdIs(agentId,pageable);
        long count = agentWechatMessageElasticRepository.countByAgentId(agentId);
        Page<AgentWechatMessage> list  =agentWechatMessageElasticRepository.findByAgentId(agentId,pageable);
        return "query wechat" ;
    }
}