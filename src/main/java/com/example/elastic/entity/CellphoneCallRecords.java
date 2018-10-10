package com.example.elastic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  ------------------
 * 2018/9/11    刘节                 Created
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Document(collection = "T_AGENT_CALL_RECORDS")
public class CellphoneCallRecords {


    @Id
    private String id;
    private String agentId;
    private Date receiveTime;
    private List<CellPhoneCallRecord> records;

    public CellphoneCallRecords() {
        this.id = UUID.randomUUID().toString();
        this.receiveTime = new Date();
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setRecords(List<CellPhoneCallRecord> records) {
        this.records = records;
    }

}
