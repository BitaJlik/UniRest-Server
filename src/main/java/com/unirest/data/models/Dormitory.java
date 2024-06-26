package com.unirest.data.models;

import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "dormitories")
public class Dormitory implements IProviderId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private boolean hasElevator;

    private CookerType cookerType;

    @OneToMany(mappedBy = "dormitory")
    private List<Floor> floors = new ArrayList<>();

    @OneToOne
    private User commandant;

}



