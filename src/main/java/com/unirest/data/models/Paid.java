package com.unirest.data.models;

import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "paids")
@Getter
@Setter
public class Paid implements IProviderId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long date;

    private double balance;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Dormitory dormitory;
}
