package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.enums.ComponentType;

import java.util.UUID;


@Getter
@Setter
public class ComponentOptionResponseDto {
    private UUID id;
    private String name;
    private ComponentType componentType;
    private int priceDelta;
}
