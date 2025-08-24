package com.Aeb.AebDMS.app.workflows.service;

import com.Aeb.AebDMS.app.workflows.model.Workflow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IWorkflowService {
    
    Workflow saveWorkflow(Workflow workflow);
    
    List<Workflow> findAll();
    
    Page<Workflow> findAll(Pageable pageable);
    
    Optional<Workflow> findById(Long id);
    
    Workflow updateWorkflow(Workflow workflow);
    
    void deleteById(Long id);
}
