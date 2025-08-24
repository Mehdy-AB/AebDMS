package com.Aeb.AebDMS.app.workflows.service.impl;

import com.Aeb.AebDMS.app.workflows.model.TaskInstance;
import com.Aeb.AebDMS.app.workflows.service.ITaskInstanceService;
import com.Aeb.AebDMS.app.workflows.repository.TaskInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskInstanceServiceImpl implements ITaskInstanceService {

    private final TaskInstanceRepository taskinstanceRepository;

    @Override
    public TaskInstance saveTaskInstance(TaskInstance taskinstance) {
        return taskinstanceRepository.save(taskinstance);
    }

    @Override
    public List<TaskInstance> findAll() {
        return taskinstanceRepository.findAll();
    }

    @Override
    public Page<TaskInstance> findAll(Pageable pageable) {
        return taskinstanceRepository.findAll(pageable);
    }

    @Override
    public Optional<TaskInstance> findById(Long id) {
        return taskinstanceRepository.findById(id);
    }

    @Override
    public TaskInstance updateTaskInstance(TaskInstance taskinstance) {
        return taskinstanceRepository.save(taskinstance);
    }

    @Override
    public void deleteById(Long id) {
        taskinstanceRepository.deleteById(id);
    }
}
