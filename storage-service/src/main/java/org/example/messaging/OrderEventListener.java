package org.example.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.RabbitMQConfig;
import org.example.domain.enums.CarStatus;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.dto.request.CreateCustomAssemblyOrderRequestDto;
import org.example.dto.request.CreateStockAssemblyOrderRequestDto;
import org.example.event.OrderApprovedEvent;
import org.example.event.OrderRejectedEvent;
import org.example.event.OrderSentForApprovalEvent;
import org.example.infrastructure.persistence.entities.cars.CarEntity;
import org.example.infrastructure.persistence.entities.processedMessages.ProcessedMessageEntity;
import org.example.repository.jpa.CarJpaRepository;
import org.example.repository.jpa.ProcessedMessageJpaRepository;
import org.example.service.AssemblyOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    private final AssemblyOrderService assemblyOrderService;
    private final CarJpaRepository carJpaRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final ProcessedMessageJpaRepository processedMessageJpaRepository;

    @RabbitListener(queues = RabbitMQConfig.ORDER_SENT_FOR_APPROVAL_QUEUE)
    public void handleOrderSentForApproval(String message) {
        try {
            OrderSentForApprovalEvent event = objectMapper.readValue(message, OrderSentForApprovalEvent.class);
            if (processedMessageJpaRepository.existsByMessageId(event.getMessageId())) {
                log.info("Message already processed: {}", event.getMessageId());
                return;
            }
            ProcessedMessageEntity processed = new ProcessedMessageEntity();
            processed.setMessageId(event.getMessageId());
            processed.setProcessedAt(Instant.now());
            processedMessageJpaRepository.save(processed);
            log.info("[trace={}] Processing order: {}", event.getTraceId(), event.getOrderId());
            if ("STOCK".equals(event.getOrderType())) {
                handleStockOrder(event);
            } else {
                handleCustomOrder(event);
            }
        } catch (Exception e) {
            log.error("Failed to process event: {}", e.getMessage());
        }
    }

    private void handleStockOrder(OrderSentForApprovalEvent event) {
        try {
            CarEntity car = carJpaRepository.findById(event.getCarId())
                    .orElseThrow(() -> new EntityNotFoundException("Car not found"));
            if (car.getStatus() != CarStatus.AVAILABLE) {
                reject(event, "Car is not available");
                return;
            }
            car.setStatus(CarStatus.RESERVED);
            carJpaRepository.save(car);
            CreateStockAssemblyOrderRequestDto dto = new CreateStockAssemblyOrderRequestDto();
            dto.setSourceOrderId(event.getOrderId());
            dto.setCarId(event.getCarId());
            assemblyOrderService.createStockAssemblyOrder(dto);
            OrderApprovedEvent approved = new OrderApprovedEvent(event.getOrderId(), event.getTraceId());
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_APPROVAL_QUEUE, objectMapper.writeValueAsString(approved));
            log.info("[trace={}] Stock order approved: {}", event.getTraceId(), event.getOrderId());
        } catch (Exception e) {
            reject(event, e.getMessage());
        }
    }

    private void handleCustomOrder(OrderSentForApprovalEvent event) {
        try {
            CreateCustomAssemblyOrderRequestDto dto = new CreateCustomAssemblyOrderRequestDto();
            dto.setSourceOrderId(event.getOrderId());
            dto.setCarModelId(event.getCarModelId());
            dto.setRequiredComponents(event.getComponentIds());
            assemblyOrderService.createCustomAssemblyOrder(dto);
            OrderApprovedEvent approved = new OrderApprovedEvent(event.getOrderId(), event.getTraceId());
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_APPROVAL_QUEUE, objectMapper.writeValueAsString(approved));
            log.info("[trace={}] Custom order approved: {}", event.getTraceId(), event.getOrderId());
        } catch (Exception e) {
            reject(event, e.getMessage());
        }
    }

    private void reject(OrderSentForApprovalEvent event, String reason) {
        try {
            OrderRejectedEvent rejected = new OrderRejectedEvent(event.getOrderId(), event.getTraceId(), reason);
            rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_REJECTED_QUEUE, objectMapper.writeValueAsString(rejected));
            log.info("[trace={}] Order rejected: {}", event.getTraceId(), event.getOrderId());
        } catch (Exception e) {
            log.error("Failed to send reject event: {}", e.getMessage());
        }
    }
}