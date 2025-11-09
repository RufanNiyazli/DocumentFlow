package com.project.documentflow.controller;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IApprovalController {
    public ResponseEntity<?> handleDecision(Long documentId, Map<String, String> payload);
}
