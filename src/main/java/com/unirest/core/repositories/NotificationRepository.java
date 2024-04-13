package com.unirest.core.repositories;

import com.unirest.core.models.Notification;
import com.unirest.core.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverAndRead(User receiver, boolean read);

    List<Notification> findByReceiverAndReceived(User receiver, boolean received);

    List<Notification> findAllBySenderOrReceiver(User sender, User receiver);
}
