package org.example.domain.models.cars;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.enums.BodyType;
import org.example.domain.enums.ComponentType;
import org.example.domain.enums.DriveType;
import org.example.domain.enums.FuelType;
import org.example.domain.models.components.ComponentOption;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor(force = true)
public class CarModel {
    private final UUID id;
    private final int basePrice;
    private final String brand;
    private final String model;
    private final BodyType bodyType;
    private final FuelType fuelType;
    private final int enginePower;
    private final int engineCapacity;
    private final DriveType driveType;
    @Getter(AccessLevel.NONE)
    private final Map<ComponentType, ComponentOption> baseConfiguration;

    public CarModel(int basePrice, String brand, String model, BodyType bodyType, FuelType fuelType, int enginePower,
                    int engineCapacity, DriveType driveType, Map<ComponentType, ComponentOption> baseConfiguration) {
        this.id = UUID.randomUUID();
        this.basePrice = basePrice;
        this.brand = brand;
        this.model = model;
        this.bodyType = bodyType;
        this.fuelType = fuelType;
        this.enginePower = enginePower;
        this.engineCapacity = engineCapacity;
        this.driveType = driveType;
        this.baseConfiguration = baseConfiguration;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarModel other)) return false;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    public Map<ComponentType, ComponentOption> getBaseConfiguration() {
        return Collections.unmodifiableMap(baseConfiguration);
    }
}
