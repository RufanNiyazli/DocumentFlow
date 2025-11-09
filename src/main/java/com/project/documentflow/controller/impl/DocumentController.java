package com.project.documentflow.controller.impl;

import com.project.documentflow.controller.IDocumentController;
import com.project.documentflow.entity.Document;
import com.project.documentflow.entity.User;
import com.project.documentflow.repository.UserRepository;
import com.project.documentflow.service.IDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DocumentController implements IDocumentController {

    private final IDocumentService documentService;
    private final UserRepository userRepository;

    @PostMapping("/public/submit")
    @Override
    public ResponseEntity<?> submitDocument(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file,
            @RequestParam("submitter") String submitter
    ) {
        try {

            User user = userRepository.findByUsername(submitter)
                    .orElseThrow(() -> new RuntimeException("User not found: " + submitter));

            // Document yarat
            Document doc = new Document();
            doc.setTitle(title);
            doc.setFilename(file.getOriginalFilename());
            doc.setSubmitter(user);
            doc.setCreatedAt(Instant.now());
            doc.setUpdatedAt(Instant.now());

            Document saved = documentService.submitDocument(doc);

            return ResponseEntity.ok(Map.of(
                    "message", "Sənəd uğurla yükləndi",
                    "documentId", saved.getId(),
                    "title", saved.getTitle(),
                    "status", saved.getStatus()
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Sənəd yüklənmədi: " + e.getMessage()));
        }
    }
}