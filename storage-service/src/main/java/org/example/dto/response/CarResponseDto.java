package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.enums.CarStatus;

import java.util.UUID;

@Getter
@Setter
public class CarResponseDto {
    private UUID id;
    private CarModelResponseDto carModel;
    private String color;
    private CarStatus status;
}
