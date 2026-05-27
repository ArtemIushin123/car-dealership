package org.example.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSentForApprovalEvent {
    private UUID orderId;
    private UUID traceId;
    private String orderType;
    private UUID carId;
    private UUID carModelId;
    private Set<UUID> componentIds;
    private UUID messageId;
}
