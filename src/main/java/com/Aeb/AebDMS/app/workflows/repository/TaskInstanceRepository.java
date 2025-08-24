package com.Aeb.AebDMS.app.workflows.repository;

import com.Aeb.AebDMS.app.workflows.model.TaskInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskInstanceRepository extends JpaRepository<TaskInstance, Long> {
    // Add custom query methods here
}
