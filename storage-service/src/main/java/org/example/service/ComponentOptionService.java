package org.example.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.enums.ComponentType;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.dto.request.CreateComponentOptionRequestDto;
import org.example.dto.response.ComponentOptionResponseDto;
import org.example.infrastructure.persistence.entities.cars.CarModelEntity;
import org.example.infrastructure.persistence.entities.components.ComponentOptionEntity;
import org.example.repository.jpa.CarModelJpaRepository;
import org.example.repository.jpa.ComponentOptionJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ComponentOptionService {
    private final ComponentOptionJpaRepository componentOptionJpaRepository;
    private final CarModelJpaRepository carModelJpaRepository;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN')")
    public ComponentOptionResponseDto create(CreateComponentOptionRequestDto dto) {
        ComponentOptionEntity entity = new ComponentOptionEntity();
        entity.setName(dto.getName());
        entity.setComponentType(dto.getComponentType());
        entity.setPriceDelta(dto.getPriceDelta());
        componentOptionJpaRepository.save(entity);
        return modelMapper.map(entity, ComponentOptionResponseDto.class);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN', 'MANAGER', 'USER')")
    public ComponentOptionResponseDto findById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        return componentOptionJpaRepository.findById(id)
                .map(e -> modelMapper.map(e, ComponentOptionResponseDto.class))
                .orElseThrow(() -> new EntityNotFoundException("ComponentOption with id " + id + " not found"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN', 'MANAGER', 'USER')")
    public List<ComponentOptionResponseDto> findAll() {
        return componentOptionJpaRepository.findAll().stream()
                .map(e -> modelMapper.map(e, ComponentOptionResponseDto.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN', 'MANAGER', 'USER')")
    public List<ComponentOptionResponseDto> findByType(ComponentType type) {
        return componentOptionJpaRepository.findByComponentType(type).stream()
                .map(e -> modelMapper.map(e, ComponentOptionResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<ComponentOptionResponseDto> findCompatibleOptionsByType(UUID carModelId, ComponentType type) {
        CarModelEntity carModelEntity = carModelJpaRepository.findById(carModelId)
                .orElseThrow(() -> new EntityNotFoundException("CarModel with id " + carModelId + " not found"));
        return componentOptionJpaRepository.findByCompatibleModelsAndComponentType(carModelEntity, type).stream()
                .map(e -> modelMapper.map(e, ComponentOptionResponseDto.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN')")
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        ComponentOptionEntity entity = componentOptionJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ComponentOption not found"));
        entity.setRemoved(true);
        componentOptionJpaRepository.save(entity);
    }
}