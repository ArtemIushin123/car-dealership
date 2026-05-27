package org.example.domain.models.orders;

import lombok.Getter;
import org.example.domain.enums.OrderStatus;
import org.example.domain.models.peoples.Client;
import org.example.domain.models.peoples.Manager;

import java.util.UUID;

@Getter
public abstract class Order {
    private final UUID id;
    private final Client client;
    private final Manager manager;
    private OrderStatus orderStatus;

    public Order(Client client, Manager manager) {
        this.id = UUID.randomUUID();
        this.client = client;
        this.manager = manager;
        this.orderStatus = OrderStatus.CREATED;
    }

    public abstract void changeOrderStatus(OrderStatus newStatus);
    protected void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
