package com.unirest.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "requests_templates")
public class RequestTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean visible;
    private int level;

    private String keysJson;

    private String name;
    private String templateName;

    private long registerTime;

    @ManyToOne
    private Dormitory dormitory;
}
