package com.project.documentflow.controller.impl;

import com.project.documentflow.controller.IApprovalController;
import com.project.documentflow.enums.Status;
import com.project.documentflow.service.IApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class ApprovalController implements IApprovalController {
    private final IApprovalService approvalService;
    @Override
    @PostMapping("/{documentId}/decision")
    public ResponseEntity<?> handleDecision(@PathVariable Long documentId,
                                            @RequestBody Map<String, String> payload) {
        try {
            Status decision = Status.valueOf(payload.get("decision"));
            String comment = payload.getOrDefault("comment", "");
            String approver = payload.get("approver");

            approvalService.handleDecision(documentId,  decision, approver, comment);
            return ResponseEntity.ok("Decision processed successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Xəta: " + e.getMessage());
        }

}

}
