package com.unirest.data.dto;

import com.unirest.data.models.Washer;
import lombok.Data;

@Data
public class WasherDTO {

    private Long id;

    private boolean isBusy;
    private long busyTo;

    private long lastUse;

    private Long floor;
    private Long user;

    public WasherDTO(){

    }

    public WasherDTO(Washer washer) {
        this.id = washer.getId();
        this.isBusy = washer.isBusy();

        this.lastUse = washer.getLastUse();
        this.busyTo = washer.getBusyTo();

        this.floor = washer.getFloor().getId();
        if (washer.getUser() != null) {
            this.user = washer.getUser().getId();
        } else {
            this.user = 0L;
        }
    }

}
