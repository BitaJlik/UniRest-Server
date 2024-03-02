package com.unirest.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unirest.core.utils.IProviderId;
import com.unirest.core.utils.IToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

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

    private int balance;

    private String uuid;
    private String username;

    private String lastName;
    private String name;
    private String surName;

    private String password;
    private String email;
    private int course;

    @OneToOne
    private Session session;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Room room;

    @OneToMany(mappedBy = "user")
    private List<Request> requests;

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
