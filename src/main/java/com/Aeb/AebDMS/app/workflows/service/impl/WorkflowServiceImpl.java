package com.Aeb.AebDMS.app.workflows.service.impl;

import com.Aeb.AebDMS.app.workflows.model.Workflow;
import com.Aeb.AebDMS.app.workflows.service.IWorkflowService;
import com.Aeb.AebDMS.app.workflows.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkflowServiceImpl implements IWorkflowService {

    private final WorkflowRepository workflowRepository;

    @Override
    public Workflow saveWorkflow(Workflow workflow) {
        return workflowRepository.save(workflow);
    }

    @Override
    public List<Workflow> findAll() {
        return workflowRepository.findAll();
    }

    @Override
    public Page<Workflow> findAll(Pageable pageable) {
        return workflowRepository.findAll(pageable);
    }

    @Override
    public Optional<Workflow> findById(Long id) {
        return workflowRepository.findById(id);
    }

    @Override
    public Workflow updateWorkflow(Workflow workflow) {
        return workflowRepository.save(workflow);
    }

    @Override
    public void deleteById(Long id) {
        workflowRepository.deleteById(id);
    }
}
