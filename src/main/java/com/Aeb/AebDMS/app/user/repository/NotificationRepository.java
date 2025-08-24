package com.Aeb.AebDMS.app.user.repository;

import com.Aeb.AebDMS.app.user.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Add custom query methods here
}
