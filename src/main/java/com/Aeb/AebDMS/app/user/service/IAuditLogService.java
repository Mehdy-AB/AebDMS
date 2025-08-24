package com.Aeb.AebDMS.app.user.service;

import com.Aeb.AebDMS.app.user.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IAuditLogService {
    
    AuditLog saveAuditLog(AuditLog auditlog);
    
    List<AuditLog> findAll();
    
    Page<AuditLog> findAll(Pageable pageable);
    
    Optional<AuditLog> findById(Long id);
    
    AuditLog updateAuditLog(AuditLog auditlog);
    
    void deleteById(Long id);
}
