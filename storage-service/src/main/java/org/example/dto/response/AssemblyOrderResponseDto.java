package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.enums.AssemblyOrderStatus;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class AssemblyOrderResponseDto {
    private UUID id;
    private UUID sourceOrderId;
    private Set<ComponentOptionResponseDto> requiredComponents;
    private UUID warehouseAdminId;
    private AssemblyOrderStatus status;
}
