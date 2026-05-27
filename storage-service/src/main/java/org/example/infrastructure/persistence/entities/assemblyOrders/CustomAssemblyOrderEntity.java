package org.example.infrastructure.persistence.entities.assemblyOrders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.infrastructure.persistence.entities.cars.CarModelEntity;
import org.example.infrastructure.persistence.entities.components.ComponentOptionEntity;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "custom_assembly_orders")
public class CustomAssemblyOrderEntity extends AssemblyOrderEntity {
    @ManyToOne
    @JoinColumn(name = "car_model_id")
    private CarModelEntity carModel;
    @ManyToMany
    @JoinTable(
            name = "custom_assembly_order_components",
            joinColumns = @JoinColumn(name = "custom_order_id"),
            inverseJoinColumns = @JoinColumn(name = "component_option_id")
    )
    private Set<ComponentOptionEntity> requiredOptions;
}
