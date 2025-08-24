package com.Aeb.AebDMS.app.user.service;

import com.Aeb.AebDMS.app.user.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    
    Notification saveNotification(Notification notification);
    
    List<Notification> findAll();
    
    Page<Notification> findAll(Pageable pageable);
    
    Optional<Notification> findById(Long id);
    
    Notification updateNotification(Notification notification);
    
    void deleteById(Long id);
}
