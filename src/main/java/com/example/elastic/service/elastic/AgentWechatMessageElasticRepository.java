package com.example.elastic.service.elastic;

import com.example.elastic.entity.AgentWechatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  ------------------
 * 2018/10/9    刘节                 Created
 */
public interface AgentWechatMessageElasticRepository extends ElasticsearchRepository<AgentWechatMessage,String> {

    List<AgentWechatMessage> findByAgentIdAndContent(String agentId, String content, Pageable pageable);

    List<AgentWechatMessage> findByAgentIdOrContent(String agentId, String content, Pageable pageable);

    List<AgentWechatMessage> findByAgentIdIs (String agentId,Pageable pageable);

    Page<AgentWechatMessage> findByAgentId(String agentId, Pageable pageable);

    List<AgentWechatMessage> findByAgentIdAndContentLike (String agentId,String content,Pageable pageable);

    long countByAgentId(String agentId);

    int deleteByAgentIdAndContent(String agentId,String content);

    @Override
    void deleteById(String id);

}
