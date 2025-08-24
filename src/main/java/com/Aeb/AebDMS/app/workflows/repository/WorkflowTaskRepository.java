package com.Aeb.AebDMS.app.workflows.repository;

import com.Aeb.AebDMS.app.workflows.model.WorkflowTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowTaskRepository extends JpaRepository<WorkflowTask, Long> {
    // Add custom query methods here
}
