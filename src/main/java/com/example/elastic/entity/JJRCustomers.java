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
 * 2018/3/22    刘节                 Created
 */
@Data
@Table(name = "t_jjr_customer")
@Entity
@Document(indexName = "lxgs",type = "customer")
public class JJRCustomers implements Serializable {

   @Id
   private String id ;

   private String agentId;

   private String agentName ;

   private String previousAgentName;

   private long userId;

   private String userName ;

   private byte realnameStatus ;

   private String phone ;

   private String addStarPhone ;

   private String addStarCardNo ;

   private String cardNo;

   private String constellation;

   private String minzu ;

   private String jiguan;

   private byte sex ;

//   private String expectArea ;

   private String expectCity ;

   private Date birthday ;

   private Date registerTime ;

   private String referrer ;

   private String grade ;

   private String rule ;

   private String source ;

   private byte status ;

   private byte releaseStatus ;

   private Date commentTime ;

   private Date matchTime ;

   private String weixin ;

   private String nickName ;

   private Date createTime ;

   private Date updateTime ;

}
