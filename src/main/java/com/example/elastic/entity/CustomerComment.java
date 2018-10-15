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
 * 2018/10/15    刘节                 Created
 */
@Data
@Table(name = "t_jjr_customer_comment")
@Entity
@Document(indexName = "customer",type = "comment")
public class CustomerComment implements Serializable {

    @Id
    private String id ;

    private String operatorId;

    private String operatorName;

    private Long userId ;

    private String userName ;

    private Byte type ;

    private String comment ;

    private Date createTime ;

    private Date updateTime;
}
