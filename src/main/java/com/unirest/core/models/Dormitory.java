package com.unirest.core.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "dormitories")
@Entity
public class Dormitory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @OneToMany
    private List<Floor> floors;

    private boolean hasElevator;

}
