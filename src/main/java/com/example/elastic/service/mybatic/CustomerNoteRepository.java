package com.example.elastic.service.mybatic;

import com.example.elastic.entity.CustomerNote;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CustomerNoteRepository extends Repository<CustomerNote,String> {

    List<CustomerNote> findAllByUserIdBetween(long startUserId, long endUserId);
}
