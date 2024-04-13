package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.models.Room;
import com.unirest.core.models.dto.DTORoom;
import com.unirest.core.repositories.RoomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController extends BaseController<Room, Long, DTORoom, RoomRepository> {

    public RoomController(RoomRepository roomRepository) {
        super(roomRepository, Room.class, DTORoom.class);
    }

    @GetMapping("/of")
    public ResponseEntity<?> of(@RequestParam("id") Long floorId) {
        List<Room> allByFloorId = repository.findAllByFloorId(floorId);
        if (allByFloorId.isEmpty()) {
             return ResponseEntity.notFound().build();
        }
        List<DTORoom> floors = new ArrayList<>();

        for (Room floor : allByFloorId) {
            floors.add(new DTORoom(floor));
        }
        return ResponseEntity.ok(floors);
    }

}
