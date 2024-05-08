package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.data.models.Floor;
import com.unirest.data.dto.FloorDTO;
import com.unirest.core.repositories.FloorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/floor")
public class FloorController extends BaseController<Floor, Long, FloorDTO, FloorRepository> {

    public FloorController(FloorRepository repository) {
        super(repository, Floor.class, FloorDTO.class);
    }

    @GetMapping("/of")
    public ResponseEntity<?> of(@RequestParam("id") Long dormitoryId) {
        List<Floor> allByDormitoryId = repository.findAllByDormitoryId(dormitoryId);
        if (allByDormitoryId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<FloorDTO> floors = new ArrayList<>();

        for (Floor floor : allByDormitoryId) {
            floors.add(new FloorDTO(floor));
        }
        return ResponseEntity.ok(floors);
    }

}
