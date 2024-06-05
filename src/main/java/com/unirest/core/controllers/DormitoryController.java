package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.data.models.Dormitory;
import com.unirest.data.dto.DormitoryDTO;
import com.unirest.core.repositories.DormitoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/dormitory")
public class DormitoryController extends BaseController<Dormitory, Long, DormitoryDTO, DormitoryRepository> {
    public DormitoryController(DormitoryRepository dormitoryRepository) {
        super(dormitoryRepository, Dormitory.class, DormitoryDTO.class);
    }

    @PostMapping("/admin/update")
    public ResponseEntity<?> updateData(DormitoryDTO dormitoryDTO) {
        Optional<Dormitory> byId = repository.findById(dormitoryDTO.getId());
        if (byId.isPresent()) {
            Dormitory dormitory = byId.get();
            if (dormitoryDTO.getName() != null) {
                dormitory.setName(dormitoryDTO.getName());
            }
            if (dormitoryDTO.getAddress() != null) {
                dormitory.setAddress(dormitoryDTO.getAddress());
            }
            if (dormitoryDTO.getCookerType() != null) {
                dormitory.setCookerType(dormitoryDTO.getCookerType());
            }
            dormitory.setHasElevator(dormitory.isHasElevator());
        }
        return ResponseEntity.badRequest().build();
    }

}
