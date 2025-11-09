package com.project.documentflow.service.impl;

import com.project.documentflow.entity.Document;
import com.project.documentflow.enums.Role;
import com.project.documentflow.enums.Status;
import com.project.documentflow.repository.DocumentRepository;
import com.project.documentflow.repository.UserRepository;
import com.project.documentflow.service.IAuditLogService;
import com.project.documentflow.service.IDocumentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService implements IDocumentService {
    private final DocumentRepository documentRepository;
    private final IAuditLogService auditLogService;
    private final UserRepository userRepository;
    private final MessageChannel documentSubmissionChannel;


    public DocumentService(DocumentRepository documentRepository, IAuditLogService auditLogService, UserRepository userRepository,@Qualifier("documentSubmissionChannel") MessageChannel documentSubmissionChannel) {
        this.documentRepository = documentRepository;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
        this.documentSubmissionChannel = documentSubmissionChannel;
    }

    @Override
    public Document submitDocument(Document document) {
        document.setStatus(Status.PENDING_APPROVAL);
        document.setCreatedAt(Instant.now());
        document.setUpdatedAt(Instant.now());

        Document save = documentRepository.save(document);
        String user = (save.getSubmitter() != null) ? save.getSubmitter().getUsername() : "system";
        auditLogService.log(save.getId(), Status.SUBMITTED, "Document Submitted", user);

        Map<String, Object> payload = new HashMap<>();
        payload.put("documentId", save.getId());
        payload.put("title", save.getTitle());
        payload.put("filename", save.getFilename());

        List<String> approvers = userRepository.findEmailsByRole(Role.APPROVER);

        Message<Map<String, Object>> msg = MessageBuilder.withPayload(payload).setHeader("approvers", approvers).build();
        documentSubmissionChannel.send(msg);
        return save;
    }
}
