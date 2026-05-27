package org.example.domain.models.assemblyOrders;


import lombok.Getter;
import org.example.domain.enums.AssemblyOrderStatus;
import org.example.domain.models.components.ComponentOption;

import java.util.Set;
import java.util.UUID;

@Getter
public abstract class AssemblyOrder {
    private final UUID id;
    private final UUID sourceOrderId;
    private final UUID warehouseAdminId;
    private AssemblyOrderStatus status;

    public AssemblyOrder(UUID sourceOrderId, UUID warehouseAdminId) {
        this.id = UUID.randomUUID();
        this.sourceOrderId = sourceOrderId;
        this.warehouseAdminId = warehouseAdminId;
    }
    protected void setAssemblyOrderStatus(AssemblyOrderStatus status){
        this.status = status;
    }
}
