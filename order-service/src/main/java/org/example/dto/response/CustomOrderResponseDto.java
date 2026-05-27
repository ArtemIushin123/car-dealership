package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class CustomOrderResponseDto extends OrderResponseDto {
    private UUID carModelId;
    private Set<UUID> selectedOptionsId;
}
