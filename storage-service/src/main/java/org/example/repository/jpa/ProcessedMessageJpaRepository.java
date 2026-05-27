package org.example.repository.jpa;

import org.example.infrastructure.persistence.entities.processedMessages.ProcessedMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedMessageJpaRepository extends JpaRepository<ProcessedMessageEntity, UUID> {
    boolean existsByMessageId(UUID messageId);
}
