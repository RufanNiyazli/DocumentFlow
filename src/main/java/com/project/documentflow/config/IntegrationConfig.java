package com.project.documentflow.config;

import com.project.documentflow.entity.Document;
import com.project.documentflow.enums.Status;
import com.project.documentflow.repository.DocumentRepository;
import com.project.documentflow.service.IAuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.*;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class IntegrationConfig {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private IAuditLogService auditLogService;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Bean
    public MessageChannel documentSubmissionChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel emailNotificationChannel() {
        return new QueueChannel(10);
    }

    @Bean
    public MessageChannel approvalDecisionChannel() {
        return new DirectChannel();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        return Pollers.fixedDelay(2000).getObject();
    }

    @Bean
    public IntegrationFlow documentSubmissionFlow() {
        return IntegrationFlow.from("documentSubmissionChannel")
                .log(msg -> "📩 New document sent: " + msg.getPayload())

                .<Map<String, Object>>handle((payload, headers) -> {
                    try {
                        List<String> approvers = (List<String>) headers.get("approvers");

                        if (approvers == null || approvers.isEmpty()) {
                            System.err.println("⚠️ No approvers found!");
                            return null;
                        }

                        return approvers.stream()
                                .map(email -> {
                                    Map<String, Object> emailPayload = new HashMap<>(payload);
                                    emailPayload.put("recipient", email);
                                    return emailPayload;
                                })
                                .toList();

                    } catch (Exception e) {
                        System.err.println("❌ Error in documentSubmissionFlow: " + e.getMessage());
                        return null;
                    }
                })

                .split()
                .channel("emailNotificationChannel")
                .get();
    }

    @Bean
    public IntegrationFlow emailNotificationFlow() {
        return IntegrationFlow.from("emailNotificationChannel")
                .log(msg -> "📧 Preparing email: " + msg.getPayload())

                .enrichHeaders(h -> h
                        .header("mail_from", fromAddress)
                        .header("mail_subject", "Yeni Sənəd Təsdiqi")
                        .headerFunction("mail_to", message -> {
                            Map<String, Object> payload = (Map<String, Object>) message.getPayload();
                            return payload.get("recipient").toString();
                        })
                )

                .transform(message -> {
                    Map<String, Object> payload = (Map<String, Object>) message;
                    return "Yeni sənəd təsdiq üçün göndərilib: " + payload.get("title") +
                            "\nFayl: " + payload.get("filename") +
                            "\nZəhmət olmasa sistemi açıb baxın.";
                })

                .log(message -> "✅ Sending email to: " + message.getHeaders().get("mail_to"))

                .handle(Mail.outboundAdapter("smtp.gmail.com")
                        .port(587)
                        .protocol("smtp")
                        .credentials(fromAddress, mailPassword)
                        .javaMailProperties(p -> {
                            p.put("mail.smtp.auth", "true");
                            p.put("mail.smtp.starttls.enable", "true");
                            p.put("mail.debug", "false");
                        })
                )
                .get();
    }

    @Bean
    public IntegrationFlow approvalDecisionFlow() {
        return IntegrationFlow.from("approvalDecisionChannel")
                .log(msg -> "📬 Decision received: " + msg.getPayload())

                .handle((payload, headers) -> {
                    try {
                        Map<String, Object> map = (Map<String, Object>) payload;
                        Long docId = Long.valueOf(map.get("documentId").toString());
                        Status decision = (Status) map.get("decision");
                        String approver = map.get("approver").toString();
                        String comment = (String) map.get("comment");

                        Document doc = documentRepository.findById(docId)
                                .orElseThrow(() -> new RuntimeException("Document not found: " + docId));

                        doc.setStatus(decision);
                        documentRepository.save(doc);

                        auditLogService.log(docId, decision, comment, approver);

                        System.out.println("✅ Document status updated to: " + decision);

                    } catch (Exception e) {
                        System.err.println("❌ Error in approvalDecisionFlow: " + e.getMessage());
                    }

                    return null;
                })

                .get();
    }
}