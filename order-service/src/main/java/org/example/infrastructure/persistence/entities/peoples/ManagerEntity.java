package org.example.infrastructure.persistence.entities.peoples;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("MANAGER")
public class ManagerEntity extends UserEntity {
}
