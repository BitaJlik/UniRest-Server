package com.unirest.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "rooms")
public class Room implements Comparable<Room>, IProviderId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int roomNumber;
    private int availableBeds;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<User> users;

    private String notes;

    @ManyToOne(cascade = CascadeType.ALL)
    private Floor floor;

    @Override
    public int compareTo(Room room) {
        return Integer.compare(roomNumber, room.getRoomNumber());
    }
}
