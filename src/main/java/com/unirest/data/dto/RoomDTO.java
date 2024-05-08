package com.unirest.data.dto;

import com.unirest.data.models.Room;
import com.unirest.data.models.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomDTO {
    private Long id;
    private int beds;
    private int number;
    private List<Long> users = new ArrayList<>();
    private String notes;

    public RoomDTO(Room room) {
        this.id = room.getId();
        this.beds = room.getAvailableBeds();
        this.number = room.getRoomNumber();
        this.notes = room.getNotes();
        for (User user : room.getUsers()) {
            users.add(user.getId());
        }
    }
}