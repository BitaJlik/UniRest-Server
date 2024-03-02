package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.core.models.Room;
import com.unirest.core.models.dto.DTORoom;
import com.unirest.core.repositories.RoomRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
public class RoomController extends BaseController<Room,Long, DTORoom, RoomRepository> {

    public RoomController(RoomRepository roomRepository) {
        super(roomRepository, Room.class, DTORoom.class);

    }

}
