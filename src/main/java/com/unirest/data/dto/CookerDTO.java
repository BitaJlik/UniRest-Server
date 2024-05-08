package com.unirest.data.dto;

import com.unirest.data.models.Cooker;
import lombok.Data;

@Data
public class CookerDTO {
    private Long id;
    private boolean isBusy;
    private long busyTo;
    private long lastUse;
    private Long floor;

    public CookerDTO(Cooker cooker) {
        this.id = cooker.getId();
        this.isBusy = cooker.isBusy();
        this.busyTo = cooker.getBusyTo();
        this.lastUse = cooker.getLastUse();
        this.floor = cooker.getFloor().getId();
    }
}
