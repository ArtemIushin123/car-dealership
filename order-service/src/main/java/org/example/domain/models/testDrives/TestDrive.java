package org.example.domain.models.testDrives;

import lombok.Getter;
import org.example.domain.enums.TestDriveStatus;
import org.example.domain.exceptions.DomainValidationException;
import org.example.domain.exceptions.IncorrectStatusTransitionException;
import org.example.domain.models.peoples.Client;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class TestDrive {
    private final UUID id;
    private final Client client;
    private final UUID carId;
    private LocalDateTime startTime;
    private TestDriveStatus testDriveStatus;

    public TestDrive(Client client, UUID carId, LocalDateTime startTime) {
        this.id = UUID.randomUUID();
        this.client = client;
        this.carId = carId;
        this.startTime = startTime;
        this.testDriveStatus = TestDriveStatus.CREATED;
        checkValidTime();
    }

    private void checkValidTime(){
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new DomainValidationException("Start time cannot be in the past");
        }
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        checkValidTime();
    }
    public void changeTestDriveStatus(TestDriveStatus newTestDriveStatus) {
        boolean valid = switch (testDriveStatus){
            case CREATED -> newTestDriveStatus == TestDriveStatus.CONFIRMED || newTestDriveStatus == TestDriveStatus.CANCELLED;
            case CONFIRMED ->  newTestDriveStatus == TestDriveStatus.READY || newTestDriveStatus == TestDriveStatus.CANCELLED;
            case READY -> newTestDriveStatus == TestDriveStatus.COMPLETED || newTestDriveStatus == TestDriveStatus.CANCELLED;
            default -> false;
        };
        if (!valid) {
            throw new IncorrectStatusTransitionException(
                    "Status " + testDriveStatus + " cannot be changed to " + newTestDriveStatus
            );
        }
        this.testDriveStatus = newTestDriveStatus;
    }

}
