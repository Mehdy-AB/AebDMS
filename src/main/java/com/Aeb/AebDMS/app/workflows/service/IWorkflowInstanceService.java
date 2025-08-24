package com.Aeb.AebDMS.app.workflows.service;

import com.Aeb.AebDMS.app.workflows.model.WorkflowInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IWorkflowInstanceService {
    
    WorkflowInstance saveWorkflowInstance(WorkflowInstance workflowinstance);
    
    List<WorkflowInstance> findAll();
    
    Page<WorkflowInstance> findAll(Pageable pageable);
    
    Optional<WorkflowInstance> findById(Long id);
    
    WorkflowInstance updateWorkflowInstance(WorkflowInstance workflowinstance);
    
    void deleteById(Long id);
}
