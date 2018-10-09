package com.example.elastic.service.mybatic;

import com.example.elastic.entity.JJRCustomers;

import org.springframework.data.repository.Repository;
import java.util.List;


public interface CustomerRepository extends Repository<JJRCustomers,String> {

    List<JJRCustomers> findByUserId(long userId);



    List<JJRCustomers> queryAllByUserIdBetween(long startUserId, long endUserId);
}
