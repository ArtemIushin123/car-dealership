package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class CreateCustomOrderRequestDto {
    private UUID clientId;
    private UUID carModelId;
    private Set<UUID> selectedOptionsId;
}
