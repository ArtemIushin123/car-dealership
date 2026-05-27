package org.example.infrastructure.persistence.entities.peoples;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.infrastructure.persistence.entities.BaseEntity;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
@Table(name = "users")
public abstract class UserEntity extends BaseEntity {
    private String name;
    @Column(name = "external_id")
    private UUID externalId;
}
