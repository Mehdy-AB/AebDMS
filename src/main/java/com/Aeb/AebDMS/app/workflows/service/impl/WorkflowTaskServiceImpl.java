package com.Aeb.AebDMS.app.workflows.service.impl;

import com.Aeb.AebDMS.app.workflows.model.WorkflowTask;
import com.Aeb.AebDMS.app.workflows.service.IWorkflowTaskService;
import com.Aeb.AebDMS.app.workflows.repository.WorkflowTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkflowTaskServiceImpl implements IWorkflowTaskService {

    private final WorkflowTaskRepository workflowtaskRepository;

    @Override
    public WorkflowTask saveWorkflowTask(WorkflowTask workflowtask) {
        return workflowtaskRepository.save(workflowtask);
    }

    @Override
    public List<WorkflowTask> findAll() {
        return workflowtaskRepository.findAll();
    }

    @Override
    public Page<WorkflowTask> findAll(Pageable pageable) {
        return workflowtaskRepository.findAll(pageable);
    }

    @Override
    public Optional<WorkflowTask> findById(Long id) {
        return workflowtaskRepository.findById(id);
    }

    @Override
    public WorkflowTask updateWorkflowTask(WorkflowTask workflowtask) {
        return workflowtaskRepository.save(workflowtask);
    }

    @Override
    public void deleteById(Long id) {
        workflowtaskRepository.deleteById(id);
    }
}
