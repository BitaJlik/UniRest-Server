package com.unirest.data.models;

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

    @OneToOne
    private Request request;
}
