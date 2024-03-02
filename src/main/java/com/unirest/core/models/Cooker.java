package com.unirest.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "cookers")
public class Cooker implements IProviderId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isBusy;

    private long lastUse;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Floor floor;
}
