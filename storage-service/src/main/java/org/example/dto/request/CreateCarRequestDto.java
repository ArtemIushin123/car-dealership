package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateCarRequestDto {
    private UUID carModelId;
    private String color;
}
