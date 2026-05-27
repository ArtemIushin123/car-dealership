package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.enums.TestDriveStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TestDriveResponseDto {
    private UUID id;
    private UserResponseDto client;
    private UUID carId;
    private LocalDateTime startTime;
    private TestDriveStatus testDriveStatus;
}
