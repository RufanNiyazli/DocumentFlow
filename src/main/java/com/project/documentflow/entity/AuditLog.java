package com.project.documentflow.entity;

import com.project.documentflow.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long documentId;

    @Enumerated(EnumType.STRING)
    private Status action;

    @Column(length = 2000)
    private String detail;

    private Instant createdAt;

    private String performedBy;
}
