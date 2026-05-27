package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class CreateCustomAssemblyOrderRequestDto {
    private UUID sourceOrderId;
    private UUID carModelId;
    private Set<UUID> requiredComponents;
}
