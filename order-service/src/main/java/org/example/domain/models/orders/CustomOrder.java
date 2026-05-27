package org.example.domain.models.orders;

import lombok.Getter;
import org.example.domain.enums.OrderStatus;
import org.example.domain.exceptions.IncorrectStatusTransitionException;
import org.example.domain.models.peoples.Client;
import org.example.domain.models.peoples.Manager;

import java.util.UUID;

@Getter
public class CustomOrder extends Order {
    private final UUID carModelId;
    public CustomOrder(Client client, Manager manager, UUID carModelId) {
        super(client, manager);
        this.carModelId = carModelId;;
    }

    @Override
    public void changeOrderStatus(OrderStatus newStatus) {
        boolean valid = switch (getOrderStatus()) {
            case CREATED -> newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.APPROVED;
            case APPROVED -> newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.WAITING_PAYMENT;
            case WAITING_PAYMENT -> newStatus == OrderStatus.PAID;
            case PAID -> newStatus == OrderStatus.WAITING_CAR_DELIVERY;
            case WAITING_CAR_DELIVERY -> newStatus == OrderStatus.READY;
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
