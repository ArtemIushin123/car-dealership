package org.example.repository.jpa;

import org.example.infrastructure.persistence.entities.cars.CarModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface CarModelJpaRepository extends JpaRepository<CarModelEntity, UUID>, JpaSpecificationExecutor<CarModelEntity> {
    List<CarModelEntity> findByBrand(String brand);
}
