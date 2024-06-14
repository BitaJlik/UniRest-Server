package com.unirest.data.dto;

import com.unirest.data.models.Request;
import lombok.Data;

@Data
public class RequestDTO {
    private Long id;

    private long date;

    private long user;
    private long dormitory;
    public RequestDTO(Request request) {
        this.id = request.getId();
        this.date = request.getDate();
        this.user = request.getUser().getId();
        this.dormitory = request.getDormitory().getId();
    }
}