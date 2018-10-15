package com.example.elastic.service.mybatic;

import com.example.elastic.entity.CustomerComment;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CustomerCommentRepository extends JpaRepository<CustomerComment,String> {


}
