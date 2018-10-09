package com.example.elastic.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  ------------------
 * 2018/10/9    刘节                 Created
 */
@Data
@Table(name = "t_jjr_agent_wechat_message")
@Entity
@Document(indexName = "wechat",type = "message")
public class AgentWechatMessage  implements Serializable{

    @Id
    private String id  ;

    private String agentId ;

    private String agentName ;

    private String agentNickName;

    private String agentWechatId ;

    private Byte type ;

    private Byte status ;

    private Byte send ;

    private Date sendTime ;

    private String talker ;

    private String content ;

    private Date createTime ;

    private Date updateTime ;



}
