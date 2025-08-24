package com.Aeb.AebDMS.app.user.service;

import com.Aeb.AebDMS.app.user.model.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IReminderService {
    
    Reminder saveReminder(Reminder reminder);
    
    List<Reminder> findAll();
    
    Page<Reminder> findAll(Pageable pageable);
    
    Optional<Reminder> findById(Long id);
    
    Reminder updateReminder(Reminder reminder);
    
    void deleteById(Long id);
}
