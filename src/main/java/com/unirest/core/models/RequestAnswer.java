package com.unirest.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "requests_answers")
public class RequestAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private long created;

    private long date;

    private boolean accepted;

    @JsonIgnore
    @OneToOne
    private Request request;
}
