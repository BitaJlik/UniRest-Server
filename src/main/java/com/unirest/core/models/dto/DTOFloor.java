package com.unirest.core.models.dto;

import com.unirest.core.models.Cooker;
import com.unirest.core.models.Floor;
import com.unirest.core.models.Room;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DTOFloor {
    private Long id;
    private int number;
    private String shortName;
    private Floor.FloorSide floorSide;
    private List<Long> rooms = new ArrayList<>();
    private List<Long> cookers = new ArrayList<>();

    public DTOFloor(Floor floor) {
        this.id = floor.getId();
        this.number = floor.getFloorNumber();
        this.shortName = floor.getShortName();
        this.floorSide = floor.getFloorSide();

        for (Room room : floor.getRooms()) {
            rooms.add(room.getId());
        }

        for (Cooker cooker : floor.getCookers()) {
            cookers.add(cooker.getId());
        }
    }
}