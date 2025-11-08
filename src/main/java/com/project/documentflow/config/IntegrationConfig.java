package com.project.documentflow.config;

import com.project.documentflow.service.IAuditLogService;
import com.project.documentflow.service.IDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.Map;

@Configuration
@IntegrationComponentScan
@RequiredArgsConstructor
public class IntegrationConfig {
    private final IAuditLogService auditLogService;
    private final IDocumentService documentService;

    @Value("${spring.mail.username}")
    private String fromAddress;

    //********//
    //Channel//
    //*******//
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


    public IntegrationFlow documentSubmissionFlow() {
        return IntegrationFlow.from("documentSubmissionChannel")
                .log(msg -> "New document sent: " + msg.getPayload())
                .<Map<String, Object>, List<String>>transform(payload -> (List<String>) msgApprovers(payload)).split()
                .channel("emailNotificationChannel").get();
    }










    private List<String> msgApprovers(Map<String, Object> payload) {
        return (List<String>) payload.get("approvers");
    }
}
