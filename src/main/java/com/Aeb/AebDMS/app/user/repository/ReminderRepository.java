package com.Aeb.AebDMS.app.user.repository;

import com.Aeb.AebDMS.app.user.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    // Add custom query methods here
}
