package org.example.repository.jpa;

import org.example.domain.enums.TestDriveStatus;
import org.example.infrastructure.persistence.entities.testDrives.TestDriveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TestDriveJpaRepository extends JpaRepository<TestDriveEntity, UUID> {
    List<TestDriveEntity> findByClientId(UUID clientId);
    List<TestDriveEntity> findByCarId(UUID carId);
    List<TestDriveEntity> findByTestDriveStatus(TestDriveStatus status);
    List<TestDriveEntity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<TestDriveEntity> findByStartTimeAfter(LocalDateTime date);
    List<TestDriveEntity> findByStartTimeBefore(LocalDateTime date);
    boolean existsByClientIdAndCarIdAndStartTime(UUID clientId, UUID carId, LocalDateTime date);
    long countByCarIdAndStartTimeBetween(UUID carId, LocalDateTime start, LocalDateTime end);
}
