package com.example.elastic.service.elastic;

import com.example.elastic.entity.AgentWechatMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  ------------------
 * 2018/10/9    刘节                 Created
 */
public interface AgentWechatMessageElasticRepository extends ElasticsearchRepository<AgentWechatMessage,String> {
}
