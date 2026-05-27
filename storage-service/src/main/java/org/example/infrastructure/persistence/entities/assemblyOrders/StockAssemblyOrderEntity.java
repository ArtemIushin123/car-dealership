package org.example.infrastructure.persistence.entities.assemblyOrders;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.infrastructure.persistence.entities.cars.CarEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "stock_assembly_orders")
public class StockAssemblyOrderEntity extends AssemblyOrderEntity{
    @ManyToOne
    @JoinColumn(name = "car_id")
    private CarEntity car;
}
