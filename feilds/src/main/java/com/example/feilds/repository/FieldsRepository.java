package com.example.feilds.repository;

import com.example.feilds.model.Fields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldsRepository extends JpaRepository<Fields, Integer> {
}
