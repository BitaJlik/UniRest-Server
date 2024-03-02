package com.unirest.core.models.dto;

import com.unirest.core.models.Request;
import lombok.Data;

@Data
public class DTORequest {
    private Long id;

    private String header;
    private Request.RequestType type;
    private long date;

    private long user;
    private long dormitory;

    public DTORequest(Request request) {
        this.id = request.getId();
        this.header = request.getHeader();
        this.type = request.getType();
        this.date = request.getDate();
        this.user = request.getUser().getId();
        this.dormitory = request.getDormitory().getId();
    }
}