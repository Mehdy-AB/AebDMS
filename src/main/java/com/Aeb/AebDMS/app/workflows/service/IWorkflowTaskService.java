package com.Aeb.AebDMS.app.workflows.service;

import com.Aeb.AebDMS.app.workflows.model.WorkflowTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IWorkflowTaskService {
    
    WorkflowTask saveWorkflowTask(WorkflowTask workflowtask);
    
    List<WorkflowTask> findAll();
    
    Page<WorkflowTask> findAll(Pageable pageable);
    
    Optional<WorkflowTask> findById(Long id);
    
    WorkflowTask updateWorkflowTask(WorkflowTask workflowtask);
    
    void deleteById(Long id);
}
