package com.example.elastic.service.mybatic;

import com.example.elastic.entity.AgentWechatMessage;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.List;

/**
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  ------------------
 * 2018/10/9    刘节                 Created
 */
public interface AgentWechatMessageRepository extends Repository<AgentWechatMessage,String> {

    List<AgentWechatMessage> findAgentWechatMessageBySendTimeBetweenOrderBySendTime(Date startTime, Date endTime);
}
