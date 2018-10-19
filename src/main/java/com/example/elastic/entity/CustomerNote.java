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
 * 2018/10/19   刘节                 Created
 */
@Data
@Table(name = "t_jjr_customer_note")
@Entity
@Document(indexName = "note",type = "customer")
public class CustomerNote  implements Serializable{

    @Id
    private String id ;
    private long userId ;
    private String name;
    private byte sex;
    private String age ;
    private String education ;
    private byte onboarding ;
    private String currentSalary ;
    private String expectSalary ;
    private String expectProvince ;
    private String expectCity ;
    private String expectCityName;
    private String expectArea ;
    private String personalityTags ;
    private int skill;
    private String careerObjective ;
    private String expectJoinTime;
    private String weixin;
    private Date weixinTime ;
    private String weixinOperator ;
    private String qq ;
    private Date qqTime ;
    private String qqOperator;
    private String tag;
    private String operatorId;
    private String communicationOperator;
    private Date communicationTime;
    private String giveUpOperator;
    private Date giveUpTime;
    private String note;
    private String marital;
    private String currentCity;
    private String currentProvince;
    private String currentArea;
    private String birthplaceProvince;
    private String birthplaceCity;
    private String birthplaceArea;
    private String goOut;
    private String findJob;
    private String peoples;
    private String painPoints;
    private Date createTime ;
    private Date updateTime;
}
