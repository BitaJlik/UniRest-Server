package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.data.models.Room;
import com.unirest.data.dto.RoomDTO;
import com.unirest.core.repositories.RoomRepository;
import com.unirest.data.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController extends BaseController<Room, Long, RoomDTO, RoomRepository> {

    public RoomController(RoomRepository roomRepository) {
        super(roomRepository, Room.class, RoomDTO.class);
    }

    @GetMapping("/of")
    public ResponseEntity<?> of(@RequestParam("id") Long floorId) {
        List<Room> allByFloorId = repository.findAllByFloorId(floorId);
        if (allByFloorId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<RoomDTO> floors = new ArrayList<>();

        for (Room floor : allByFloorId) {
            floors.add(new RoomDTO(floor));
        }
        return ResponseEntity.ok(floors);
    }

}
