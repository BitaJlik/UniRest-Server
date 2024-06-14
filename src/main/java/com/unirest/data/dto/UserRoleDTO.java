package com.unirest.data.dto;

import com.unirest.data.models.UserRole;
import lombok.Data;

@Data
public class UserRoleDTO {
    private Long id;

    private String name;

    private int level;

    private Long dormitory;

    public UserRoleDTO(UserRole role) {
        this.id = role.getId();
        this.name = role.getName();
        this.level = role.getLevel();
        this.dormitory = role.getDormitory().getId();
    }


}
