package com.Aeb.AebDMS.app.user.service.impl;

import com.Aeb.AebDMS.app.user.model.Reminder;
import com.Aeb.AebDMS.app.user.service.IReminderService;
import com.Aeb.AebDMS.app.user.repository.ReminderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements IReminderService {

    private final ReminderRepository reminderRepository;

    @Override
    public Reminder saveReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @Override
    public List<Reminder> findAll() {
        return reminderRepository.findAll();
    }

    @Override
    public Page<Reminder> findAll(Pageable pageable) {
        return reminderRepository.findAll(pageable);
    }

    @Override
    public Optional<Reminder> findById(Long id) {
        return reminderRepository.findById(id);
    }

    @Override
    public Reminder updateReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @Override
    public void deleteById(Long id) {
        reminderRepository.deleteById(id);
    }
}
