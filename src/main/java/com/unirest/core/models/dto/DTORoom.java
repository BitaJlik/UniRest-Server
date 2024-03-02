package com.unirest.core.models.dto;

import com.unirest.core.models.Room;
import com.unirest.core.models.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DTORoom {
    private Long id;
    private int beds;
    private int number;
    private List<Long> users = new ArrayList<>();
    private String notes;

    public DTORoom(Room room) {
        this.id = room.getId();
        this.beds = room.getAvailableBeds();
        this.number = room.getRoomNumber();
        this.notes = room.getNotes();
        for (User user : room.getUsers()) {
            users.add(user.getId());
        }
    }
}