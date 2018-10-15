package com.example.elastic.service.elastic;

import com.example.elastic.entity.CustomerComment;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CustomerCommentElasticRepository extends ElasticsearchRepository<CustomerComment,String> {


    List<CustomerComment> findAllByUserId (long userId, Sort sort);
}
