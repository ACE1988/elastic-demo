package com.example.elastic.service.elastic;

import com.example.elastic.entity.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EmployeeRepository extends ElasticsearchRepository<Employee,Long> {


}
