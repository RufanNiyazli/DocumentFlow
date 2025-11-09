package com.project.documentflow.service;

import com.project.documentflow.enums.Status;

public interface IApprovalService {
    public void handleDecision(Long docId, Status decision, String approver, String comment);
}
