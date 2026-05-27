package org.example.infrastructure.persistence.entities.components;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.enums.ComponentType;
import org.example.infrastructure.persistence.entities.BaseEntity;
import org.example.infrastructure.persistence.entities.cars.CarModelEntity;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "component_options")
public class ComponentOptionEntity extends BaseEntity {
    private String name;
    @Enumerated(EnumType.STRING)
    private ComponentType componentType;
    private int priceDelta;
    @ManyToMany
    @JoinTable(
            name = "component_option_compatible_models",
            joinColumns = @JoinColumn(name = "component_option_id"),
            inverseJoinColumns = @JoinColumn(name = "car_model_id")
    )
    private Set<CarModelEntity> compatibleModels;
}
