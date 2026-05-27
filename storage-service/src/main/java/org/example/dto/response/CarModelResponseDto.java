package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.enums.BodyType;
import org.example.domain.enums.DriveType;
import org.example.domain.enums.FuelType;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class CarModelResponseDto {
    private UUID id;
    private int price;
    private String brand;
    private String model;
    private BodyType bodyType;
    private FuelType fuelType;
    private int enginePower;
    private int engineCapacity;
    private DriveType driveType;
    private Set<ComponentOptionResponseDto> baseConfiguration;
}
