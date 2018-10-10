package com.example.elastic.service.mybatic;

import com.example.elastic.entity.JJRCustomers;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.annotation.PostConstruct;
import java.util.List;


public interface CustomerRepository extends Repository<JJRCustomers,String> {

    List<JJRCustomers> findByUserId(long userId);

    List<JJRCustomers> queryAllByUserIdBetween(long startUserId, long endUserId);

    @Modifying
    @Query("update t_jjr_customer cc set cc.deleted = 1 where cc.user_id = :userId and cc.agent_id :agentId")
    int updateByUserIdAndAgentId(@Param("userId") long userId,@Param("agentId") String agentId);
}
