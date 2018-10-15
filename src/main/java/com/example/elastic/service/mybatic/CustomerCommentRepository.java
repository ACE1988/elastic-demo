package com.example.elastic.service.mybatic;

import com.example.elastic.entity.CustomerComment;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface CustomerCommentRepository extends Repository<CustomerComment,String> {

    List<CustomerComment> findAllByUserIdBetween(long startUserId, long endUserId);

    List<CustomerComment> findAllByUserId(long userId);
}
