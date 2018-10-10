package com.example.elastic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;

/**
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  ------------------
 * 2018/9/10    刘节                 Created
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class CellPhoneCallRecord {

    private Long duration ;

    private String name ;

    private Date date ;

    private Integer type ;

    private String number ;
}
