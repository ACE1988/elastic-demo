package com.example.elastic.service.elastic;


import com.example.elastic.entity.CustomerNote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomerNoteElasticRepository  extends ElasticsearchRepository<CustomerNote,String> {
}
