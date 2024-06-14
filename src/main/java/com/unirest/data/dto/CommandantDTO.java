package com.unirest.data.dto;

import com.unirest.data.models.User;
import lombok.Data;

@Data
public class CommandantDTO {
    private String name;
    private String lastName;
    private String phone;
    private Long lastActive;

    public CommandantDTO() {

    }

    public CommandantDTO(User user) {
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.phone = user.getPhoneNumber();
        this.lastActive = user.getLastActive();
    }
}
