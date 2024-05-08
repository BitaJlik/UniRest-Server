package com.unirest.core.controllers;

import com.unirest.core.controllers.base.BaseController;
import com.unirest.data.models.Notification;
import com.unirest.data.models.User;
import com.unirest.data.dto.NotificationDTO;
import com.unirest.core.repositories.NotificationRepository;
import com.unirest.core.services.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notification")
public class NotificationController extends BaseController<Notification, Long, NotificationDTO, NotificationRepository> {

    private final UserService userService;

    @Autowired
    public NotificationController(NotificationRepository repository, UserService userService) {
        super(repository, Notification.class, NotificationDTO.class);
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@RequestParam("id") Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            List<Notification> notifications = repository.findAllBySenderOrReceiver(user, user);
            List<NotificationDTO> notificationList = new ArrayList<>();
            for (Notification notification : notifications) {
                if (!notification.isReceived()) {
                    notification.setReceived(true);
                    repository.saveAndFlush(notification);
                }
                notificationList.add(new NotificationDTO(notification));
            }
            return ResponseEntity.ok(notificationList);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/receive")
    public ResponseEntity<?> receiveNotification(@RequestParam("id") Long notificationId) {
        Optional<Notification> byId = repository.findById(notificationId);
        if (byId.isPresent()) {
            Notification notification = byId.get();
            notification.setReceived(true);
            repository.save(notification);
            return ResponseEntity.status(HttpStatus.ACCEPTED.value()).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/read")
    public ResponseEntity<?> readNotification(@RequestParam("id") Long notificationId) {
        Optional<Notification> byId = repository.findById(notificationId);
        if (byId.isPresent()) {
            Notification notification = byId.get();
            notification.setRead(true);
            repository.save(notification);
            return ResponseEntity.status(HttpStatus.ACCEPTED.value()).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/call", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> callUsers(@RequestParam("id") Long senderId, @RequestBody NotificationRequest request) {
        if (request.users != null && !request.users.isEmpty()) {
            User sender = userService.findById(senderId);
            if (sender != null) {
                for (User user : request.users) {
                    Notification notification = new Notification();
                    notification.setDate(System.currentTimeMillis());
                    notification.setSender(sender);
                    notification.setReceiver(user);
                    notification.setTitle(request.notificationTemplate.getTitle());
                    notification.setContent(request.notificationTemplate.getContent());
                    repository.save(notification);
                }
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @Data
    public static class NotificationRequest {
        private Notification notificationTemplate;
        private List<User> users;
    }

}
