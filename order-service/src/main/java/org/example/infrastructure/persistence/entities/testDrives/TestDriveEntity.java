package org.example.infrastructure.persistence.entities.testDrives;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.domain.enums.TestDriveStatus;
import org.example.infrastructure.persistence.entities.BaseEntity;
import org.example.infrastructure.persistence.entities.peoples.ClientEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "test_drives")
public class TestDriveEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;
    @Column(name = "car_id")
    private UUID carId;
    private LocalDateTime startTime;
    @Enumerated(EnumType.STRING)
    private TestDriveStatus testDriveStatus;
}
