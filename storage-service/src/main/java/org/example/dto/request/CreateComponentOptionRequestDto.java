package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.enums.ComponentType;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class CreateComponentOptionRequestDto {
    private String name;
    private ComponentType componentType;
    private int priceDelta;
    private Set<UUID> compatibleModels;
}
