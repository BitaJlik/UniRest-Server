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
    private Long user;

    public CookerDTO(Cooker cooker) {
        this.id = cooker.getId();
        this.isBusy = cooker.isBusy();
        this.busyTo = cooker.getBusyTo();
        this.lastUse = cooker.getLastUse();
        this.floor = cooker.getFloor().getId();
        if (cooker.getUser() != null) {
            this.user = cooker.getUser().getId();
        } else {
            this.user = 0L;
        }
    }
}
