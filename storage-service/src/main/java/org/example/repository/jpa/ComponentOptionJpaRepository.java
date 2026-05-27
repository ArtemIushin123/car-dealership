package org.example.repository.jpa;

import org.example.domain.enums.ComponentType;
import org.example.infrastructure.persistence.entities.cars.CarModelEntity;
import org.example.infrastructure.persistence.entities.components.ComponentOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ComponentOptionJpaRepository extends JpaRepository<ComponentOptionEntity, UUID> {
    List<ComponentOptionEntity> findByName(String name);
    List<ComponentOptionEntity> findByComponentType(ComponentType type);
    List<ComponentOptionEntity> findByCompatibleModels(CarModelEntity carModel);
    List<ComponentOptionEntity> findByCompatibleModelsAndComponentType(CarModelEntity carModel, ComponentType type);
}
