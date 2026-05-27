package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.infrastructure.persistence.entities.orders.OrderEntity;
import org.example.repository.jpa.OrderJpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("orderSecurity")
@RequiredArgsConstructor
public class OrderSecurity {
    private final OrderJpaRepository orderJpaRepository;
    public boolean isOwner(UUID orderId) {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        String subject = auth.getToken().getSubject();
        OrderEntity order = orderJpaRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("Order not found"));
        UUID externalId = order.getClient().getExternalId();
        if (externalId == null) {
            return false;
        }
        return subject.equals(externalId.toString());
    }
}
