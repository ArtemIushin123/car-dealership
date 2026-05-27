package org.example.infrastructure.persistence.entities.cars;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.enums.BodyType;
import org.example.domain.enums.DriveType;
import org.example.domain.enums.FuelType;
import org.example.infrastructure.persistence.entities.BaseEntity;
import org.example.infrastructure.persistence.entities.components.ComponentOptionEntity;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "car_models")
public class CarModelEntity extends BaseEntity {
    private int price;
    private String brand;
    private String model;
    @Enumerated(EnumType.STRING)
    private BodyType bodyType;
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;
    private int enginePower;
    private int engineCapacity;
    @Enumerated(EnumType.STRING)
    private DriveType driveType;
    @ManyToMany
    @JoinTable(
            name = "car_model_base_components",
            joinColumns = @JoinColumn(name = "car_model_id"),
            inverseJoinColumns = @JoinColumn(name = "component_option_id")
    )
    private Set<ComponentOptionEntity> baseConfiguration;
}
