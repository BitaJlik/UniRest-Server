package com.unirest.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users_roles")
@Getter
@Setter
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int level;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @ManyToOne
    private Dormitory dormitory;
}
