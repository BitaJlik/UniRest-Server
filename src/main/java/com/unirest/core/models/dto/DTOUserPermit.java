package com.unirest.core.models.dto;

import com.unirest.core.models.User;
import lombok.Data;

@Data
public class DTOUserPermit {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private int course;
    private int room;
    private long expire;

    public DTOUserPermit(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.course = user.getCourse();
        this.room = user.getRoom().getRoomNumber();
        this.expire = user.getExpire();
    }
}
