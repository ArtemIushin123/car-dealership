package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CarConfigurationResponseDto {
    private CarModelResponseDto carModel;
    private Map<String, ComponentOptionResponseDto> selectedOptions;
    private int totalPrice;
}
