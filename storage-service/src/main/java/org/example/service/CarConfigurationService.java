package org.example.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.enums.ComponentType;
import org.example.domain.exceptions.DomainValidationException;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.domain.models.cars.CarConfiguration;
import org.example.domain.models.cars.CarModel;
import org.example.domain.models.components.ComponentOption;
import org.example.dto.response.CarConfigurationResponseDto;
import org.example.dto.response.CarModelResponseDto;
import org.example.dto.response.ComponentOptionResponseDto;
import org.example.infrastructure.persistence.entities.cars.CarModelEntity;
import org.example.infrastructure.persistence.entities.components.ComponentOptionEntity;
import org.example.repository.jpa.CarModelJpaRepository;
import org.example.repository.jpa.ComponentOptionJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CarConfigurationService {

    private final CarModelJpaRepository carModelJpaRepository;
    private final ComponentOptionJpaRepository componentOptionJpaRepository;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CarConfigurationResponseDto startConfiguration(UUID carModelId) {
        Objects.requireNonNull(carModelId, "carModelId cannot be null");
        CarModelEntity entity = carModelJpaRepository.findById(carModelId)
                .orElseThrow(() -> new EntityNotFoundException("CarModel not found"));
        CarModel carModel = toCarModel(entity);
        CarConfiguration configuration = new CarConfiguration(carModel);
        for (Map.Entry<ComponentType, ComponentOption> entry : carModel.getBaseConfiguration().entrySet()) {
            configuration.selectOption(entry.getKey(), entry.getValue());
        }
        return toDto(configuration, entity);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public CarConfigurationResponseDto selectOption(UUID carModelId, ComponentType type, UUID optionId) {
        Objects.requireNonNull(carModelId, "carModelId cannot be null");
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(optionId, "optionId cannot be null");
        CarModelEntity carModelEntity = carModelJpaRepository.findById(carModelId)
                .orElseThrow(() -> new EntityNotFoundException("CarModel not found"));
        ComponentOptionEntity optionEntity = componentOptionJpaRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("ComponentOption not found"));
        if (optionEntity.getComponentType() != type) {
            throw new DomainValidationException(
                    "Component type mismatch. Expected " + type + " but got " + optionEntity.getComponentType()
            );
        }
        CarModel carModel = toCarModel(carModelEntity);
        ComponentOption option = new ComponentOption(
                optionEntity.getName(),
                optionEntity.getComponentType(),
                optionEntity.getPriceDelta()
        );
        CarConfiguration configuration = new CarConfiguration(carModel);
        configuration.selectOption(type, option);
        return toDto(configuration, carModelEntity);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<ComponentOptionResponseDto> getAvailableOptions(UUID carModelId, ComponentType type) {
        Objects.requireNonNull(carModelId, "carModelId cannot be null");
        Objects.requireNonNull(type, "type cannot be null");
        CarModelEntity carModelEntity = carModelJpaRepository.findById(carModelId)
                .orElseThrow(() -> new EntityNotFoundException("CarModel not found"));
        return componentOptionJpaRepository
                .findByCompatibleModelsAndComponentType(carModelEntity, type).stream()
                .map(e -> modelMapper.map(e, ComponentOptionResponseDto.class))
                .collect(Collectors.toList());
    }

    private CarModel toCarModel(CarModelEntity entity) {
        Map<ComponentType, ComponentOption> baseConfig = entity.getBaseConfiguration().stream()
                .collect(Collectors.toMap(
                        ComponentOptionEntity::getComponentType,
                        e -> new ComponentOption(e.getName(), e.getComponentType(), e.getPriceDelta())
                ));
        return new CarModel(
                entity.getPrice(),
                entity.getBrand(),
                entity.getModel(),
                entity.getBodyType(),
                entity.getFuelType(),
                entity.getEnginePower(),
                entity.getEngineCapacity(),
                entity.getDriveType(),
                baseConfig
        );
    }

    private CarConfigurationResponseDto toDto(CarConfiguration configuration, CarModelEntity entity) {
        CarConfigurationResponseDto dto = new CarConfigurationResponseDto();
        dto.setCarModel(modelMapper.map(entity, CarModelResponseDto.class));
        dto.setSelectedOptions(configuration.getSelectedOptions().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        e -> modelMapper.map(e.getValue(), ComponentOptionResponseDto.class)
                )));
        dto.setTotalPrice(configuration.calculateTotalPrice());
        return dto;
    }
}
