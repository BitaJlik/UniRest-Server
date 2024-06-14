package com.unirest.data.models;

import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cookers")
public class Cooker implements IProviderId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isBusy;
    private long busyTo;

    private long lastUse;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Floor floor;

    @ManyToOne
    private User user;
}
