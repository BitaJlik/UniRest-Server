package com.unirest.data.models;

import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "payments")
public class Payment implements IProviderId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long date;
    private double balance;
    private String checkId;

    private long moderateDate;
    private boolean moderated;
    private boolean valid;

    @ManyToOne
    private User user;

    @ManyToOne
    private Dormitory dormitory;
}
