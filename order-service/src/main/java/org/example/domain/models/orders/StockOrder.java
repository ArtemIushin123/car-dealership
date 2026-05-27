package org.example.domain.models.orders;

import lombok.Getter;
import org.example.domain.enums.OrderStatus;
import org.example.domain.exceptions.IncorrectStatusTransitionException;
import org.example.domain.models.peoples.Client;
import org.example.domain.models.peoples.Manager;

import java.util.UUID;

@Getter
public class StockOrder extends Order{
    private final UUID carId;
    public StockOrder(Client client, Manager manager, UUID carId) {
        super(client, manager);
        this.carId = carId;
    }
    @Override
    public void changeOrderStatus(OrderStatus newStatus) {
        boolean valid = switch (getOrderStatus()) {
            case CREATED -> newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.APPROVED;
            case APPROVED -> newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.WAITING_PAYMENT;
            case WAITING_PAYMENT -> newStatus == OrderStatus.PAID;
            case PAID -> newStatus == OrderStatus.READY;
            case READY -> newStatus == OrderStatus.COMPLETED;
            default -> false;
        };
        if (!valid) {
            throw new IncorrectStatusTransitionException(
                    "Status " + getOrderStatus() + " cannot be changed to " + newStatus
            );
        }
        setOrderStatus(newStatus);
    }
}
