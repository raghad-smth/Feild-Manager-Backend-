package com.example.feilds.repository;

import com.example.feilds.model.FieldSlots;
import com.example.feilds.model.Fields;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeildSlotsRepository extends JpaRepository<FieldSlots, Integer> {

    void save(FieldSlots slot);
}
