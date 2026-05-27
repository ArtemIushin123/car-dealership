package org.example.infrastructure.persistence.entities.peoples;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("WAREHOUSE_ADMIN")
public class WarehouseAdminEntity extends UserEntity {
}
