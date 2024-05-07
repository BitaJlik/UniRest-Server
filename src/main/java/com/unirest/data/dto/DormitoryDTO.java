package com.unirest.data.dto;

import com.unirest.data.models.CookerType;
import com.unirest.data.models.Dormitory;
import com.unirest.data.models.Floor;
import com.unirest.data.models.Room;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DormitoryDTO {
    private Long id;

    private String name;
    private String address;
    private boolean hasElevator;

    private List<Long> floors;
    private CookerType cookerType;
    private Long commandant;

    private CommandantDTO commandantInfo;

    private int totalBeds;
    private int totalTakenBeds;
    private int totalFreeBeds;

    public DormitoryDTO(Dormitory dormitory) {
        this.id = dormitory.getId();
        this.address = dormitory.getAddress();
        this.floors = new ArrayList<>();
        this.name = dormitory.getName();

        this.totalBeds = 0;
        this.totalFreeBeds = 0;
        this.totalTakenBeds = 0;

        for (Floor floor : dormitory.getFloors()) {
            this.floors.add(floor.getId());
            for (Room room : floor.getRooms()) {
                this.totalBeds += room.getAvailableBeds();
                this.totalTakenBeds += room.getUsers().size();
            }
        }
        this.totalFreeBeds = totalBeds - totalTakenBeds;

        this.cookerType = dormitory.getCookerType();

        this.commandant = dormitory.getCommandant().getId();

        this.commandantInfo = new CommandantDTO(dormitory.getCommandant());
    }

}