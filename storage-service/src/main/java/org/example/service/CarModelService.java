package org.example.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.exceptions.DomainValidationException;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.dto.request.CreateCarModelRequestDto;
import org.example.dto.response.CarModelResponseDto;
import org.example.infrastructure.persistence.entities.cars.CarModelEntity;
import org.example.infrastructure.persistence.entities.components.ComponentOptionEntity;
import org.example.repository.jpa.CarModelJpaRepository;
import org.example.repository.jpa.ComponentOptionJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CarModelService {
    private final CarModelJpaRepository carModelJpaRepository;
    private final ComponentOptionJpaRepository componentOptionJpaRepository;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN')")
    public CarModelResponseDto create(CreateCarModelRequestDto dto) {
        Set<ComponentOptionEntity> baseConfig = dto.getBaseConfigurationIds().stream()
                .map(id -> componentOptionJpaRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("ComponentOption not found")))
                .collect(Collectors.toSet());
        CarModelEntity entity = new CarModelEntity();
        entity.setPrice(dto.getPrice());
        entity.setBrand(dto.getBrand());
        entity.setModel(dto.getModel());
        entity.setBodyType(dto.getBodyType());
        entity.setFuelType(dto.getFuelType());
        entity.setEnginePower(dto.getEnginePower());
        entity.setEngineCapacity(dto.getEngineCapacity());
        entity.setDriveType(dto.getDriveType());
        entity.setBaseConfiguration(baseConfig);
        carModelJpaRepository.save(entity);
        return modelMapper.map(entity, CarModelResponseDto.class);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN', 'MANAGER', 'USER')")
    public CarModelResponseDto findById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        return carModelJpaRepository.findById(id)
                .map(e -> modelMapper.map(e, CarModelResponseDto.class))
                .orElseThrow(() -> new EntityNotFoundException("CarModel with id " + id + " not found"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN', 'MANAGER', 'USER')")
    public List<CarModelResponseDto> findAll() {
        return carModelJpaRepository.findAll().stream()
                .map(e -> modelMapper.map(e, CarModelResponseDto.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN')")
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        CarModelEntity entity = carModelJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CarModel not found"));
        entity.setRemoved(true);
        carModelJpaRepository.save(entity);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN', 'MANAGER', 'USER')")
    public List<CarModelResponseDto> findByBrand(String brand) {
        Objects.requireNonNull(brand, "brand cannot be null");
        if (brand.isBlank()) throw new DomainValidationException("brand cannot be blank");
        return carModelJpaRepository.findByBrand(brand).stream()
                .map(e -> modelMapper.map(e, CarModelResponseDto.class))
                .collect(Collectors.toList());
    }
}
