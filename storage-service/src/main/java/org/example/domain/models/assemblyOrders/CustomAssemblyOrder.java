package org.example.domain.models.assemblyOrders;

import lombok.Getter;
import org.example.domain.models.cars.CarModel;
import org.example.domain.models.components.ComponentOption;

import java.util.Set;
import java.util.UUID;

@Getter
public class CustomAssemblyOrder extends AssemblyOrder{
    private final CarModel carModel;
    private final Set<ComponentOption> requiredComponents;

    public CustomAssemblyOrder(UUID sourceOrderId, UUID warehouseAdminId, CarModel carModel, Set<ComponentOption> requiredComponents) {
        super(sourceOrderId, warehouseAdminId);
        this.carModel = carModel;
        this.requiredComponents = requiredComponents;
    }
}
