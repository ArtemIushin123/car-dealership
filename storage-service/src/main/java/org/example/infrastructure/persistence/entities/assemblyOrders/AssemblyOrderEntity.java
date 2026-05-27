package org.example.infrastructure.persistence.entities.assemblyOrders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.enums.AssemblyOrderStatus;
import org.example.infrastructure.persistence.entities.BaseEntity;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "assembly_orders")
public abstract class AssemblyOrderEntity extends BaseEntity {
    @Column(name = "source_order_id")
    private UUID sourceOrderId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AssemblyOrderStatus assemblyOrderStatus;
}
