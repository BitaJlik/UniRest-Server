package com.unirest.data.dto;

import com.unirest.data.models.User;
import lombok.Data;

@Data
public class UserSearchDTO {
    private Long dormitoryId;
    private String name;
    private String lastName;
    private int roomNumber;
    private Long roomId;

    public UserSearchDTO(User user) {
        this.dormitoryId = user.getDormitoryId();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.roomId = user.getRoom().getId();
        this.roomNumber = user.getRoom().getRoomNumber();
    }
}
