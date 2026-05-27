package org.example.infrastructure.persistence.entities.orders;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.enums.OrderStatus;
import org.example.infrastructure.persistence.entities.BaseEntity;
import org.example.infrastructure.persistence.entities.peoples.ClientEntity;
import org.example.infrastructure.persistence.entities.peoples.ManagerEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "orders")
public abstract class OrderEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private ManagerEntity manager;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
