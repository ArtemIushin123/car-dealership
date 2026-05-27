package org.example.infrastructure.persistence.entities.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stock_orders")
public class StockOrderEntity extends OrderEntity {
    @Column(name = "car_id")
    private UUID carId;
}
