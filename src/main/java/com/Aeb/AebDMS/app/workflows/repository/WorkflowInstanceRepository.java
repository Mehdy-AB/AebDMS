package com.Aeb.AebDMS.app.workflows.repository;

import com.Aeb.AebDMS.app.workflows.model.WorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Long> {
    // Add custom query methods here
}
