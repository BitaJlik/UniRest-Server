package com.unirest.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Table(name = "floors")
@Getter
@Setter
@Entity
public class Floor implements Comparable<Floor>, IProviderId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int floorNumber;

    private String shortName;

    private FloorSide floorSide;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL)
    private List<Cooker> cookers;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "dormitory_id")
    private Dormitory dormitory;


    @Override
    public int compareTo(Floor o) {
        return Integer.compare(floorNumber, o.floorNumber);
    }

    public enum FloorSide {
        FULL, LEFT, RIGHT, CENTRAL, SMALL, LARGE, CUSTOM
    }

}
