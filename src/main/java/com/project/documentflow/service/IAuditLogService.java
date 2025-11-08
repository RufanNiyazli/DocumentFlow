package com.project.documentflow.service;

import com.project.documentflow.enums.Status;

public interface IAuditLogService {
    public void log(Long documentId, Status action, String detail, String performedBy);
}
