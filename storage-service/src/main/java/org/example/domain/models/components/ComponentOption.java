package org.example.domain.models.components;

import lombok.Getter;
import org.example.domain.enums.ComponentType;
import org.example.domain.models.cars.CarModel;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ComponentOption {
    private final UUID id;
    private final String name;
    private final ComponentType componentType;
    private final int priceDelta;
    private final Set<CarModel> compatibleModels;

    public ComponentOption(String name, ComponentType componentType, int surcharge) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.componentType = componentType;
        this.priceDelta = surcharge;
        this.compatibleModels = new HashSet<>();
    }
    public void addCompatibleModel(CarModel carModel) {
        this.compatibleModels.add(carModel);
    }
    public boolean isCompatibleWith(CarModel carModel) {
        return compatibleModels.contains(carModel);
    }
}
