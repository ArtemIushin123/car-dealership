package org.example.service;

import org.example.config.RabbitMQConfig;
import org.example.event.OrderSentForApprovalEvent;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.enums.OrderStatus;
import org.example.domain.exceptions.DomainValidationException;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.dto.request.CreateCustomOrderRequestDto;
import org.example.dto.request.CreateStockOrderRequestDto;
import org.example.dto.response.CustomOrderResponseDto;
import org.example.dto.response.OrderResponseDto;
import org.example.dto.response.StockOrderResponseDto;
import org.example.infrastructure.persistence.entities.orders.CustomOrderEntity;
import org.example.infrastructure.persistence.entities.orders.OrderEntity;
import org.example.infrastructure.persistence.entities.orders.StockOrderEntity;
import org.example.infrastructure.persistence.entities.peoples.ClientEntity;
import org.example.infrastructure.persistence.entities.peoples.ManagerEntity;
import org.example.infrastructure.persistence.entities.peoples.UserEntity;
import org.example.repository.jpa.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderJpaRepository orderJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ModelMapper modelMapper;
    private final OutboxService outboxService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public StockOrderResponseDto createStock(CreateStockOrderRequestDto dto) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
        String subject = jwtAuth.getToken().getSubject();
        UserEntity userEntity = userJpaRepository.findByExternalId(UUID.fromString(subject))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!(userEntity instanceof ClientEntity clientEntity)) {
            throw new DomainValidationException("User is not a client");
        }
        List<ManagerEntity> managers = userJpaRepository.findByType(ManagerEntity.class);
        if (managers.isEmpty()) {
            throw new DomainValidationException("No managers available");
        }
        ManagerEntity manager = managers.get(new Random().nextInt(managers.size()));
        StockOrderEntity order = new StockOrderEntity();
        order.setClient(clientEntity);
        order.setManager(manager);
        order.setOrderStatus(OrderStatus.CREATED);
        orderJpaRepository.save(order);
        return modelMapper.map(order, StockOrderResponseDto.class);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CustomOrderResponseDto createCustom(CreateCustomOrderRequestDto dto) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
        String subject = jwtAuth.getToken().getSubject();
        UserEntity userEntity = userJpaRepository.findByExternalId(UUID.fromString(subject))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<ManagerEntity> managers = userJpaRepository.findByType(ManagerEntity.class);
        if (!(userEntity instanceof ClientEntity clientEntity)) {
            throw new DomainValidationException("User is not a client");
        }
        if (managers.isEmpty()){
            throw new DomainValidationException("No managers available");
        }
        ManagerEntity manager = managers.get(new Random().nextInt(managers.size()));
        CustomOrderEntity order = new CustomOrderEntity();
        order.setClient(clientEntity);
        order.setManager(manager);
        order.setOrderStatus(OrderStatus.CREATED);
        orderJpaRepository.save(order);
        return modelMapper.map(order, CustomOrderResponseDto.class);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @orderSecurity.isOwner(#id)")
    public OrderResponseDto findById(UUID id) {
        return orderJpaRepository.findById(id)
                .map(e -> modelMapper.map(e, OrderResponseDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public List<OrderResponseDto> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream().
                anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        boolean isManagerOrAdmin = auth.getAuthorities().stream().
                anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER") ||
                        a.getAuthority().equals("ROLE_ADMIN"));
        if (isManagerOrAdmin){
            return orderJpaRepository.findAll().stream()
                    .map(e -> modelMapper.map(e, OrderResponseDto.class))
                    .collect(Collectors.toList());
        }
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
        String subject = jwtAuth.getToken().getSubject();
        return orderJpaRepository.findAll().stream()
                .filter(o -> o.getClient().getExternalId() != null &&
                        o.getClient().getExternalId().toString().equals(subject))
                .map(e -> modelMapper.map(e, OrderResponseDto.class))
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') or @orderSecurity.isOwner(#id)")
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        OrderEntity entity = orderJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        entity.setRemoved(true);
        orderJpaRepository.save(entity);
    }

    public OrderResponseDto payOrder(UUID orderId) {
        OrderEntity order =  orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setOrderStatus(OrderStatus.PAID);
        orderJpaRepository.save(order);
        OrderSentForApprovalEvent event = new OrderSentForApprovalEvent();
        event.setOrderId(orderId);
        event.setTraceId(UUID.randomUUID());
        if (order instanceof CustomOrderEntity customOrderEntity) {
            event.setOrderType("CUSTOM");
            event.setCarModelId(customOrderEntity.getCarModelId());
            event.setComponentIds(customOrderEntity.getSelectedOptions());
        }
        else if (order instanceof StockOrderEntity stockOrderEntity) {
            event.setOrderType("STOCK");
            event.setCarId(stockOrderEntity.getCarId());
        }
        outboxService.save(RabbitMQConfig.ORDER_SENT_FOR_APPROVAL_QUEUE, event);
        return modelMapper.map(order, OrderResponseDto.class);
    }
}
