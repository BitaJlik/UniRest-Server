package com.unirest.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table(name = "floors")
@Data
@Entity
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int floorNumber;

    @OneToMany
    private List<Room> rooms;

    //
    @JsonIgnore
    @ManyToOne
    private Dormitory dormitory;

}
