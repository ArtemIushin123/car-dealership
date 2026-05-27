package org.example.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.persistence.entities.outboxes.OutboxEntity;
import org.example.repository.jpa.OutboxJpaRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxScheduler {
    private final OutboxJpaRepository outboxJpaRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutbox() {
        List<OutboxEntity> events = outboxJpaRepository.findByProcessedFalse();
        for (OutboxEntity event : events) {
            try {
                rabbitTemplate.convertAndSend(event.getQueueName(), event.getPayload());
                event.setProcessed(true);
                outboxJpaRepository.save(event);
                log.info("Outbox event sent to queue: {}", event.getQueueName());
            } catch (Exception e) {
                log.error("Failed to send outbox event: {}", e.getMessage());
            }
        }
    }
}
