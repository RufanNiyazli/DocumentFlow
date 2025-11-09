package com.project.documentflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IDocumentController {
    public ResponseEntity<?> submitDocument (String title, MultipartFile file, String submitter);
}
