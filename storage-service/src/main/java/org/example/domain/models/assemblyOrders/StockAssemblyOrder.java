package org.example.domain.models.assemblyOrders;

import lombok.Getter;
import org.example.domain.models.cars.Car;
import org.example.domain.models.components.ComponentOption;

import java.util.Set;
import java.util.UUID;

@Getter
public class StockAssemblyOrder extends AssemblyOrder {
    private final Car car;
    public StockAssemblyOrder(UUID sourceOrderId, UUID warehouseAdminId, Car car) {
        super(sourceOrderId, warehouseAdminId);
        this.car = car;
    }
}
