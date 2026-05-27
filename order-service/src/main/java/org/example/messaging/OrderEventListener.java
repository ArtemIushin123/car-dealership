package org.example.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.RabbitMQConfig;
import org.example.domain.enums.OrderStatus;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.event.OrderApprovedEvent;
import org.example.event.OrderRejectedEvent;
import org.example.infrastructure.persistence.entities.orders.OrderEntity;
import org.example.repository.jpa.OrderJpaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    private final OrderJpaRepository orderJpaRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.ORDER_APPROVAL_QUEUE)
    public void handleOrderApproved(String message) {
        try {
            OrderApprovedEvent event = objectMapper.readValue(message, OrderApprovedEvent.class);
            log.info("[trace={}] Order approved: {}", event.getTraceId(), event.getOrderId());
            OrderEntity order = orderJpaRepository.findById(event.getOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));
            order.setOrderStatus(OrderStatus.READY);
            orderJpaRepository.save(order);
        } catch (Exception e) {
            log.error("Failed to process order approved event: {}", e.getMessage());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_REJECTED_QUEUE)
    public void handleOrderRejected(String message) {
        try {
            OrderRejectedEvent event = objectMapper.readValue(message, OrderRejectedEvent.class);
            log.info("[trace={}] Order rejected: {}", event.getTraceId(), event.getOrderId());
            OrderEntity order = orderJpaRepository.findById(event.getOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderJpaRepository.save(order);
        } catch (Exception e) {
            log.error("Failed to process order rejected event: {}", e.getMessage());
        }
    }
}
