package com.Aeb.AebDMS.app.workflows.service.impl;

import com.Aeb.AebDMS.app.workflows.model.WorkflowInstance;
import com.Aeb.AebDMS.app.workflows.service.IWorkflowInstanceService;
import com.Aeb.AebDMS.app.workflows.repository.WorkflowInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkflowInstanceServiceImpl implements IWorkflowInstanceService {

    private final WorkflowInstanceRepository workflowinstanceRepository;


    @Override
    public WorkflowInstance saveWorkflowInstance(WorkflowInstance workflowinstance) {
        return workflowinstanceRepository.save(workflowinstance);
    }

    @Override
    public List<WorkflowInstance> findAll() {
        return workflowinstanceRepository.findAll();
    }

    @Override
    public Page<WorkflowInstance> findAll(Pageable pageable) {
        return workflowinstanceRepository.findAll(pageable);
    }

    @Override
    public Optional<WorkflowInstance> findById(Long id) {
        return workflowinstanceRepository.findById(id);
    }

    @Override
    public WorkflowInstance updateWorkflowInstance(WorkflowInstance workflowinstance) {
        return workflowinstanceRepository.save(workflowinstance);
    }

    @Override
    public void deleteById(Long id) {
        workflowinstanceRepository.deleteById(id);
    }
}
