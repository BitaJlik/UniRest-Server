package com.unirest.data.dto;

import com.unirest.data.models.Notification;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private String title;
    private Long date;
    private String content;

    private Long sender;
    private Long receiver;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.date = notification.getDate();
        this.content = notification.getContent();
        this.sender = notification.getSender().getId();
        this.receiver = notification.getReceiver().getId();
    }

}
