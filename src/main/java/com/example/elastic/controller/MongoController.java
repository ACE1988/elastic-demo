package com.example.elastic.controller;

import com.example.elastic.Util.DateUtils;
import com.example.elastic.entity.CellPhoneCallRecord;
import com.example.elastic.entity.CellphoneCallRecords;
import com.example.elastic.service.mongo.CellPhoneCallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by lenovo on 2018/10/10.
 */
@RestController
@RequestMapping("cellPhone")
public class MongoController {

    @Autowired
    CellPhoneCallRepository cellPhoneCallRepository ;

    @RequestMapping("/save")
    public void save(){

        CellphoneCallRecords cellphoneCallRecords = new CellphoneCallRecords();
        cellphoneCallRecords.setAgentId("admin");
        cellphoneCallRecords.setId(UUID.randomUUID().toString());
        cellphoneCallRecords.setReceiveTime(new Date());
        CellPhoneCallRecord record = new CellPhoneCallRecord();
        record.setDate(DateUtils.convert("2018-09-10 23:45:42"));
        record.setDuration(10L);
        record.setName("刘节");
        record.setNumber("13818620570");
        record.setType(1);
        List<CellPhoneCallRecord> records = new ArrayList<>();
        records.add(record);
        cellphoneCallRecords.setRecords(records);
        CellphoneCallRecords sr =  cellPhoneCallRepository.save(cellphoneCallRecords);
        System.out.print("sr");

    }

   @RequestMapping("/delete")
    public void delete(@RequestParam("id") String id){
        cellPhoneCallRepository.deleteById(id);
    }

    @RequestMapping("/query")
    public void query(){
//        cellPhoneCallRepository.
    }
}
