package com.project.documentflow.service.impl;

import com.project.documentflow.entity.Approval;
import com.project.documentflow.entity.Document;
import com.project.documentflow.enums.Status;
import com.project.documentflow.repository.ApprovalRepository;
import com.project.documentflow.repository.DocumentRepository;
import com.project.documentflow.repository.UserRepository;
import com.project.documentflow.service.IApprovalService;
import com.project.documentflow.service.IAuditLogService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApprovalService implements IApprovalService {
    private final DocumentRepository documentRepository;
    private final IAuditLogService auditLogService;
    private final UserRepository userRepository;
    private final MessageChannel approvalDecisionChannel;
    private final ApprovalRepository approvalRepository;

    public ApprovalService(DocumentRepository documentRepository, IAuditLogService auditLogService, UserRepository userRepository, @Qualifier("approvalDecisionChannel") MessageChannel documentSubmissionChannel, MessageChannel approvalDecisionChannel, ApprovalRepository approvalRepository) {
        this.documentRepository = documentRepository;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
        this.approvalDecisionChannel = approvalDecisionChannel;
        this.approvalRepository = approvalRepository;
    }

    @Override
    public void handleDecision(Long docId, Status decision, String approverUsername, String comment) {
        Document doc = documentRepository.findById(docId).orElseThrow(() -> new RuntimeException("Document not found:" + docId));

        Approval approval = new Approval();
        approval.setDecision(decision);
        approval.setComment(comment);
        approval.setDecidedAt(Instant.now());
        approval.setDocument(doc);

        approvalRepository.save(approval);
        auditLogService.log(docId, decision, comment, approverUsername);
        Map<String, Object> payload = new HashMap<>();
        payload.put("documentId", docId);
        payload.put("decision", decision);
        payload.put("approver", approverUsername);
        payload.put("comment", comment);

        Message<Map<String, Object>> msg = MessageBuilder
                .withPayload(payload)
                .build();
        // burda ayrica payload yaratmaq daha tovsiye usuldur icinde yaratmaq  problemlidir
        /// =================================//
//        Message<Map<String, Object>> msg = MessageBuilder.withPayload(
//                Map.of(
//                        "documentId", documentId,
//                        "decision", decision,
//                        "approver", approverUsername,
//                        "comment", comment
//                )
//        ).build();

        /// =================================//

    }
}
