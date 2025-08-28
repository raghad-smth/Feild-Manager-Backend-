package com.example.feilds.controller;

import com.example.feilds.model.Fields;
import com.example.feilds.repository.FieldsRepository;
import com.example.feilds.service.FieldsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fields")
public class FieldsController {
    private final FieldsService fieldsService;

    public FieldsController(FieldsService fieldsService) {
        this.fieldsService = fieldsService;
    }

    @GetMapping
    public List<Fields> getFields(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minPlayers,
            @RequestParam(required = false) Boolean isActive
    ) {
        return fieldsService.browseFields(location, minPlayers, isActive);
    }
    
    @Autowired
    private FieldsRepository FieldsRepository;

    @GetMapping("/{id}/details")
    public ResponseEntity<byte[]> getFieldDetails(@PathVariable Integer id) {
        return FieldsRepository.findById(id)
                .map(field -> {
                    // Build a formatted string
                    StringBuilder details = new StringBuilder();
                    details.append("Field Details\n");
                    details.append("====================\n");
                    details.append("ID: ").append(field.getId()).append("\n");
                    details.append("Name: ").append(field.getName()).append("\n");
                    details.append("Players Capacity: ").append(field.getPlayersCapacity()).append("\n");
                    details.append("Location: ").append(field.getLocationAddress()).append("\n");
                    details.append("Active: ").append(field.getIsActive() ? "Yes" : "No").append("\n");

                    // Convert to bytes
                    byte[] fileBytes = details.toString().getBytes();

                    // Return as downloadable text file
                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=field_" + field.getId() + "_details.txt")
                            .body(fileBytes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
