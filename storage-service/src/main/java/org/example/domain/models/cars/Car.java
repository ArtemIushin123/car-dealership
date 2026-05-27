package org.example.domain.models.cars;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.enums.CarStatus;

import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class Car {
    private final UUID id;
    private final CarModel carModel;
    private final String color;
    private CarStatus status;

    public Car(CarModel carModel, String color) {
        this.id = UUID.randomUUID();
        this.carModel = carModel;
        this.color = color;
        this.status = CarStatus.AVAILABLE;
    }
    public void markSold() {
        this.status = CarStatus.SOLD;
    }

    public void markForTestDrive() {
        this.status = CarStatus.TEST_DRIVE;
    }

    public void markReserved() {
        this.status = CarStatus.RESERVED;
    }

    public void markAvailable() {
        this.status = CarStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return this.status == CarStatus.AVAILABLE;
    }

    public boolean isReserved() {
        return this.status == CarStatus.RESERVED;
    }

    public boolean isSold() {
        return this.status == CarStatus.SOLD;
    }

    public boolean isTestDrive() {
        return this.status == CarStatus.TEST_DRIVE;
    }
}
