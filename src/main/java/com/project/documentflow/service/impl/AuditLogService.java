package com.project.documentflow.service.impl;

import com.project.documentflow.entity.AuditLog;
import com.project.documentflow.enums.Status;
import com.project.documentflow.repository.AuditLogRepository;
import com.project.documentflow.service.IAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService {
    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(Long documentId, Status action, String detail, String performedBy) {
        AuditLog log = new AuditLog(null, documentId, action, detail, Instant.now(), performedBy);
        auditLogRepository.save(log);

    }
}
