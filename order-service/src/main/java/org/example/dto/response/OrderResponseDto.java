package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.enums.OrderStatus;

import java.util.UUID;

@Getter
@Setter
public class OrderResponseDto {
    private UUID id;
    private UserResponseDto client;
    private UserResponseDto manager;
    private OrderStatus orderStatus;
}
