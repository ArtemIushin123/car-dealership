package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateCarConfigurationRequestDto {
    private UUID carModelId;
    private List<UUID> selectedOptionIds;
}
