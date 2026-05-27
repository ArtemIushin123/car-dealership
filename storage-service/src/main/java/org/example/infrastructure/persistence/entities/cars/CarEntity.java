package org.example.infrastructure.persistence.entities.cars;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.enums.CarStatus;
import org.example.infrastructure.persistence.entities.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class CarEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "car_model_id")
    private CarModelEntity carModel;
    private String color;
    @Enumerated(EnumType.STRING)
    private CarStatus status;
}
