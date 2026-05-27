package org.example.domain.models.cars;

import lombok.AccessLevel;
import lombok.Getter;
import org.example.domain.enums.ComponentType;
import org.example.domain.exceptions.DomainValidationException;
import org.example.domain.exceptions.IncompatibleComponentException;
import org.example.domain.models.components.ComponentOption;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CarConfiguration {
    private final CarModel carModel;
    @Getter(AccessLevel.NONE)
    private final Map<ComponentType, ComponentOption> selectedOptions = new HashMap<>();

    public CarConfiguration(CarModel carModel) {
        this.carModel = carModel;
    }

    public void selectOption(ComponentType type, ComponentOption option) {
        if (!option.isCompatibleWith(carModel)) {
            String modelKey = buildModelKey(carModel);
            throw new IncompatibleComponentException(
                    "Component " + option.getName() + " is not compatible with " + modelKey
            );
        }
        selectedOptions.put(type, option);
    }

    public void validate() {
        for (ComponentType type : carModel.getBaseConfiguration().keySet()) {
            if (!selectedOptions.containsKey(type)) {
                throw new DomainValidationException("A required component is missing:" + type);
            }
        }
    }
    public int calculateTotalPrice() {
        int surcharge = selectedOptions.values().stream()
                .mapToInt(ComponentOption::getPriceDelta)
                .sum();
        return carModel.getBasePrice() + surcharge;
    }
    public Map<ComponentType, ComponentOption> getSelectedOptions() {
        return Collections.unmodifiableMap(selectedOptions);
    }
    private String buildModelKey(CarModel carModel) {
        return carModel.getBrand() + " " + carModel.getModel();
    }
}