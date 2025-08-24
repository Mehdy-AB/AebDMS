package com.Aeb.AebDMS.app.user.service.impl;

import com.Aeb.AebDMS.app.user.model.AuditLog;
import com.Aeb.AebDMS.app.user.service.IAuditLogService;
import com.Aeb.AebDMS.app.user.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements IAuditLogService {

    private final AuditLogRepository auditlogRepository;

    @Override
    public AuditLog saveAuditLog(AuditLog auditlog) {
        return auditlogRepository.save(auditlog);
    }

    @Override
    public List<AuditLog> findAll() {
        return auditlogRepository.findAll();
    }

    @Override
    public Page<AuditLog> findAll(Pageable pageable) {
        return auditlogRepository.findAll(pageable);
    }

    @Override
    public Optional<AuditLog> findById(Long id) {
        return auditlogRepository.findById(id);
    }

    @Override
    public AuditLog updateAuditLog(AuditLog auditlog) {
        return auditlogRepository.save(auditlog);
    }

    @Override
    public void deleteById(Long id) {
        auditlogRepository.deleteById(id);
    }
}
