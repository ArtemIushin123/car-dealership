package org.example.infrastructure.persistence.entities.peoples;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("SYSTEM_ADMIN")
public class SystemAdminEntity extends UserEntity {
}
