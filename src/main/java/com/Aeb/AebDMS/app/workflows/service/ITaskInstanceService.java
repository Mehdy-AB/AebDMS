package com.Aeb.AebDMS.app.workflows.service;

import com.Aeb.AebDMS.app.workflows.model.TaskInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ITaskInstanceService {
    
    TaskInstance saveTaskInstance(TaskInstance taskinstance);
    
    List<TaskInstance> findAll();
    
    Page<TaskInstance> findAll(Pageable pageable);
    
    Optional<TaskInstance> findById(Long id);
    
    TaskInstance updateTaskInstance(TaskInstance taskinstance);
    
    void deleteById(Long id);
}
