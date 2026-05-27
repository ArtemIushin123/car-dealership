package org.example.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.enums.BodyType;
import org.example.domain.enums.DriveType;
import org.example.domain.enums.FuelType;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class CreateCarModelRequestDto {
    private int price;
    private String brand;
    private String model;
    private BodyType bodyType;
    private FuelType fuelType;
    private int enginePower;
    private int engineCapacity;
    private DriveType driveType;
    private Set<UUID> baseConfigurationIds;
}
