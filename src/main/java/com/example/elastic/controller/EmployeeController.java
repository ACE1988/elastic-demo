package com.example.elastic.controller;

import com.example.elastic.entity.Employee;
import com.example.elastic.service.elastic.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("getOne")
    public Employee getOne(long id){
        Optional<Employee> goodsInfo = employeeRepository.findById(id);
        return goodsInfo.get();
    }

    @GetMapping("save")
    public String save(){
        List<String> in = new ArrayList<>();
        in.add("music");
        Employee employee = new Employee(4L,"jie","liu","Working with Spring Data Repositories",30,in);
        employeeRepository.save(employee);
        return "success";
    }

}
