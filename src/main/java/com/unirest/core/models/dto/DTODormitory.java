package com.unirest.core.models.dto;

import com.unirest.core.models.Dormitory;
import com.unirest.core.models.Floor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DTODormitory {
    private Long id;

    private String name;
    private String address;
    private boolean hasElevator;

    private List<Long> floors;

    private Long commandant;

    public DTODormitory(Dormitory dormitory) {
        this.id = dormitory.getId();
        this.address = dormitory.getAddress();
        this.floors = new ArrayList<>();
        this.name = dormitory.getName();

        for (Floor floor : dormitory.getFloors()) {
            floors.add(floor.getId());
        }

        this.commandant = dormitory.getCommandant().getId();
    }

}