package com.unirest.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int roomNumber;
    private int availableBeds;

    @OneToMany
    private List<User> users;

    //
    @JsonIgnore
    @ManyToOne
    private Floor floor;
}
