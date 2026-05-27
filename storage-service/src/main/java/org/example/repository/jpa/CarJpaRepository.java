package org.example.repository.jpa;

import org.example.domain.enums.BodyType;
import org.example.domain.enums.CarStatus;
import org.example.domain.enums.DriveType;
import org.example.domain.enums.FuelType;
import org.example.infrastructure.persistence.entities.cars.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CarJpaRepository extends JpaRepository<CarEntity, UUID> {
    List<CarEntity> findByStatus(CarStatus status);
    List<CarEntity> findByCarModelId(UUID modelId);
    List<CarEntity> findByCarModelBrand(String brand);
    List<CarEntity> findByCarModelBrandAndCarModelModel(String brand, String model);
    List<CarEntity> findByCarModelBodyType(BodyType bodyType);
    List<CarEntity> findByCarModelFuelType(FuelType fuelType);
    List<CarEntity> findByCarModelDriveType(DriveType driveType);
    List<CarEntity> findByColor(String color);
    List<CarEntity> findByCarModelEnginePowerGreaterThanEqual(int minPower);
    List<CarEntity> findByCarModelEngineCapacityLessThanEqual(int maxCapacity);
    List<CarEntity> findByCarModelPriceLessThanEqual(int maxPrice);
    List<CarEntity> findByCarModelPriceGreaterThanEqual(int minPrice);
}
