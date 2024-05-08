package com.unirest.data.models;

import com.unirest.core.utils.IProviderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification implements IProviderId<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private Long date;
    private String title;
    private String content;

    @Column(name = "is_read")
    private boolean read;
    @Column(name = "is_received")
    private boolean received;

}
