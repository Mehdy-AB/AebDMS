package com.Aeb.AebDMS.app.workflows.repository;

import com.Aeb.AebDMS.app.workflows.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    // Add custom query methods here
}
