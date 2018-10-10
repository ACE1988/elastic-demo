package com.example.elastic.service.mongo;


import com.example.elastic.entity.CellphoneCallRecords;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  ------------------
 * 2018/9/10    刘节                 Created
 */
public interface CellPhoneCallRepository extends MongoRepository<CellphoneCallRecords,String> {

    @Override
    Iterable<CellphoneCallRecords> findAllById(Iterable<String> strings);

    @Override
    List<CellphoneCallRecords> findAll();

    @Override
    void deleteById(String s);

    @Override
    void delete(CellphoneCallRecords entity);

    @Override
    CellphoneCallRecords  save(CellphoneCallRecords entity);


}
