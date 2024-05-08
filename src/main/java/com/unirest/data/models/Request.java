package com.unirest.data.models;

import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private User user;

    @ManyToOne
    private Dormitory dormitory;

    public enum RequestType {
        ADD, REMOVE, UPDATE
    }
}
