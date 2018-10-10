package com.example.elastic.service.mybatic;

import com.example.elastic.entity.JJRCustomers;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CustomerRepository extends Repository<JJRCustomers,String> {

    List<JJRCustomers> findByUserId(long userId);

    List<JJRCustomers> queryAllByUserIdBetween(long startUserId, long endUserId);

    //update 需要添加事务
    @Transactional
    @Modifying
    @Query("update JJRCustomers  as cc set cc.agentId = :agentId where cc.userId = :userId")
    int updateByUserIdAndAgentId(@Param("userId") long userId,@Param("agentId") String agentId);



}
