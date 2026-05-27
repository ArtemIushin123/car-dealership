package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.event.OrderSentForApprovalEvent;
import org.example.infrastructure.persistence.entities.outboxes.OutboxEntity;
import org.example.repository.jpa.OutboxJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OutboxService {
    private final OutboxJpaRepository outboxJpaRepository;
    private final ObjectMapper objectMapper;

    public void save(String queueName, Object event) {
        try {
            if (event instanceof OrderSentForApprovalEvent e) {
                e.setMessageId(UUID.randomUUID());
            }
            OutboxEntity outbox = new OutboxEntity();
            outbox.setQueueName(queueName);
            outbox.setPayload(objectMapper.writeValueAsString(event));
            outbox.setProcessed(false);
            outbox.setCreatedAt(Instant.now());
            outboxJpaRepository.save(outbox);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save outbox event", e);
        }
    }
}
