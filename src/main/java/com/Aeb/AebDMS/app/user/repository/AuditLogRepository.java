package com.Aeb.AebDMS.app.user.repository;

import com.Aeb.AebDMS.app.user.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // Add custom query methods here
}
