package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateTestDriveRequestDto {
    private UUID clientId;
    private UUID carId;
    private LocalDateTime startTime;
}
