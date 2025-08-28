package com.example.feilds.service;

import com.example.feilds.model.Fields;
import com.example.feilds.repository.FieldsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldsService {
    private final FieldsRepository fieldsRepository;

    public FieldsService(FieldsRepository fieldsRepository) {
        this.fieldsRepository = fieldsRepository;
    }

    public List<Fields> browseFields(String location, Integer minPlayers, Boolean isActive) {
        return fieldsRepository.findAll().stream()
                .filter(field -> location == null || field.getLocationAddress().toLowerCase().contains(location.toLowerCase()))
                .filter(field -> minPlayers == null || field.getPlayersCapacity() >= minPlayers)
                .filter(field -> isActive == null || field.getIsActive().equals(isActive))
                .collect(Collectors.toList());
    }
}
