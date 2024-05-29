package com.unirest.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unirest.core.utils.IProviderId;
import com.unirest.core.utils.IToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements IToken, IProviderId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double balance;

    private String username;

    private String name;
    private String lastName;
    private String surName;

    @JsonIgnore
    private String password;
    private String email;

    private String phoneNumber; // UA +38(0**)-1234-123

    private String universityName;
    private Integer course;

    private boolean emailVerified;

    @Transient
    private Long dormitoryId;

    @OneToOne
    private Session session;

    private Long expire;

    private Long lastActive;

    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "user")
    private List<Request> requests;

    @OneToMany(mappedBy = "user")
    private List<Payment> payments;

    @ManyToOne
    private UserRole role;

    public Long getDormitoryId() {
        Long dormitoryId = null;
        if (room != null && room.getFloor() != null && room.getFloor().getDormitory() != null) {
            dormitoryId = room.getFloor().getDormitory().getId();
        }
        if (dormitoryId == null) {
            dormitoryId = role.getDormitory().getId();
        }

        return dormitoryId;
    }

    @JsonIgnore
    public Dormitory getDormitory() {
        Dormitory dormitoryId = null;
        if (room != null && room.getFloor() != null && room.getFloor().getDormitory() != null) {
            dormitoryId = room.getFloor().getDormitory();
        }
        if (dormitoryId == null) {
            dormitoryId = role.getDormitory();
        }

        return dormitoryId;
    }

    @Override
    public String getSubject() {
        return email;
    }

    @Override
    public HashMap<String, ?> getValues() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return map;
    }

}
