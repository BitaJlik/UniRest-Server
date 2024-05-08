package com.unirest.data.dto;

import com.unirest.data.models.User;
import lombok.Data;

@Data
public class UserPermitDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private int course;
    private int room;
    private long expire;

    public UserPermitDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.course = user.getCourse();
        this.room = user.getRoom().getRoomNumber();
        this.expire = user.getExpire();
    }
}
