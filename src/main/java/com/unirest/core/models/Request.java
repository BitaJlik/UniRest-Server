package com.unirest.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Callable;

@Entity
@Getter
@Setter
@Table(name = "requests")
public class Request implements IProviderId<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String header;
    private RequestType type;
    private long date;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonIgnore
    private Dormitory dormitory;

    public enum RequestType {
        ADD, REMOVE, UPDATE
    }
}
