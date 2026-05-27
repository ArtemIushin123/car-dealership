package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateCustomOrderRequestDto;
import org.example.dto.request.CreateStockOrderRequestDto;
import org.example.dto.response.CustomOrderResponseDto;
import org.example.dto.response.OrderResponseDto;
import org.example.dto.response.StockOrderResponseDto;
import org.example.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN', 'USER')")
    public List<OrderResponseDto> getAll() { return orderService.findAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN') or @orderSecurity.isOwner(#id)")
    public OrderResponseDto getById(@PathVariable UUID id) { return orderService.findById(id); }

    @PostMapping("/stock")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public StockOrderResponseDto createStock(@RequestBody CreateStockOrderRequestDto dto) { return orderService.createStock(dto); }

    @PostMapping("/custom")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CustomOrderResponseDto createCustom(@RequestBody CreateCustomOrderRequestDto dto) { return orderService.createCustom(dto); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN') or (hasRole('USER') and @orderSecurity.isOwner(#id))")
    public void delete(@PathVariable UUID id) { orderService.deleteById(id); }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public OrderResponseDto pay(@PathVariable UUID id) {
        return orderService.payOrder(id);
    }
}
