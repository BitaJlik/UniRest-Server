package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.data.models.Floor;
import com.unirest.data.dto.FloorDTO;
import com.unirest.core.repositories.FloorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/admin/update")
    public ResponseEntity<?> updateData(@RequestBody FloorDTO floorDTO) {
        Optional<Floor> byId = repository.findById(floorDTO.getId());
        if (byId.isPresent()) {
            Floor floor = byId.get();
            if (floorDTO.getFloorSide() != null) {
                floor.setFloorSide(floorDTO.getFloorSide());
            }
            if (floorDTO.getShortName() != null) {
                floor.setShortName(floorDTO.getShortName());
            }
            repository.save(floor);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


}
