package org.example.repository.jpa;

import org.example.domain.enums.OrderStatus;
import org.example.infrastructure.persistence.entities.orders.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByClientId(UUID clientId);
    List<OrderEntity> findByManagerId(UUID managerId);
    List<OrderEntity> findByOrderStatus(OrderStatus status);
    List<OrderEntity> findByClientIdAndOrderStatus(UUID clientId, OrderStatus status);
    List<OrderEntity> findByManagerIdAndOrderStatus(UUID managerId, OrderStatus status);
    long countByOrderStatus(OrderStatus status);
    long countByClientId(UUID clientId);
    boolean existsByClientIdAndOrderStatus(UUID clientId, OrderStatus status);
}
