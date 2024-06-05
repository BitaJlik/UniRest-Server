package com.unirest.data.dto;

import com.unirest.data.models.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private Long dormitoryId;
    private String email;

    private double balance;
    private String username;
    private String name;
    private String lastName;
    private String surName;
    private String password;
    private int course;
    private String phoneNumber;
    private boolean emailVerified;
    private long expire;
    private RoomDTO room;
    private UserRoleDTO role;

    public UserDTO(User user) {
        this.id = user.getId();
        this.dormitoryId = user.getDormitoryId();
        this.email = user.getEmail();
        this.balance = user.getBalance();
        this.username = user.getUsername();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.surName = user.getSurName();
        this.course = user.getCourse();
        this.phoneNumber = user.getPhoneNumber();
        this.emailVerified = user.isEmailVerified();
        this.expire = user.getExpire();
        if(user.getRoom() != null){
            this.room =  new RoomDTO(user.getRoom());
        }
        if (user.getRole() != null) {
            this.role = new UserRoleDTO(user.getRole());
        }
    }
}
