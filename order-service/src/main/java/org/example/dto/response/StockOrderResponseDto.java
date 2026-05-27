package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StockOrderResponseDto extends OrderResponseDto {
    private UUID carId;
}
