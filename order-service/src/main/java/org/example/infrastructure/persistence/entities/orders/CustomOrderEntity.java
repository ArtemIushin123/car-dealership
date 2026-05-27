package org.example.infrastructure.persistence.entities.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "custom_orders")
public class CustomOrderEntity extends OrderEntity {
    @Column(name = "car_model_id")
    private UUID carModelId;
    @ElementCollection
    @CollectionTable(
            name = "custom_order_selected_components",
            joinColumns = @JoinColumn(name = "custom_order_id")
    )
    @Column(name = "component_option_id")
    private Set<UUID> selectedOptions;
}
