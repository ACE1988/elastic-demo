package com.example.elastic.controller;

import com.example.elastic.entity.CustomerNote;
import com.example.elastic.service.elastic.CustomerNoteElasticRepository;
import com.example.elastic.service.mybatic.CustomerNoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("note")
public class CustomerNoteController {

    @Autowired
    CustomerNoteRepository customerNoteRepository;

    @Autowired
    CustomerNoteElasticRepository customerNoteElasticRepository ;

    Logger LOGGER = LoggerFactory.getLogger(CustomerNoteController.class);

    @RequestMapping("query")
    public void query(@RequestParam("userId") Long userId,@RequestParam("userId2") long userId2){

        List<CustomerNote> list = customerNoteRepository.findAllByUserIdBetween(userId,userId2);
        LOGGER.info("list size={}",list.size());
        if(list.size() > 0){
            customerNoteElasticRepository.saveAll(list);
        }
    }
}
