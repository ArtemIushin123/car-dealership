package org.example.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.enums.AssemblyOrderStatus;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.dto.request.CreateCustomAssemblyOrderRequestDto;
import org.example.dto.request.CreateStockAssemblyOrderRequestDto;
import org.example.dto.response.AssemblyOrderResponseDto;
import org.example.dto.response.CustomAssemblyOrderResponseDto;
import org.example.dto.response.StockAssemblyOrderResponseDto;
import org.example.infrastructure.persistence.entities.assemblyOrders.AssemblyOrderEntity;
import org.example.infrastructure.persistence.entities.assemblyOrders.CustomAssemblyOrderEntity;
import org.example.infrastructure.persistence.entities.cars.CarEntity;
import org.example.infrastructure.persistence.entities.assemblyOrders.StockAssemblyOrderEntity;
import org.example.infrastructure.persistence.entities.cars.CarModelEntity;
import org.example.infrastructure.persistence.entities.components.ComponentOptionEntity;
import org.example.repository.jpa.AssemblyOrderJpaRepository;
import org.example.repository.jpa.CarJpaRepository;
import org.example.repository.jpa.CarModelJpaRepository;
import org.example.repository.jpa.ComponentOptionJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AssemblyOrderService {
    private final AssemblyOrderJpaRepository assemblyOrderJpaRepository;
    private final CarJpaRepository carJpaRepository;
    private final CarModelJpaRepository carModelJpaRepository;
    private final ComponentOptionJpaRepository componentOptionJpaRepository;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public StockAssemblyOrderResponseDto createStockAssemblyOrder(CreateStockAssemblyOrderRequestDto dto){
        CarEntity carEntity = carJpaRepository.findById(dto.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        StockAssemblyOrderEntity order = new StockAssemblyOrderEntity();
        order.setSourceOrderId(dto.getSourceOrderId());
        order.setAssemblyOrderStatus(AssemblyOrderStatus.CREATED);
        order.setCar(carEntity);
        assemblyOrderJpaRepository.save(order);
        return modelMapper.map(order, StockAssemblyOrderResponseDto.class);
    }

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public CustomAssemblyOrderResponseDto createCustomAssemblyOrder(CreateCustomAssemblyOrderRequestDto dto){
        CarModelEntity carModelEntity = carModelJpaRepository.findById(dto.getCarModelId())
                .orElseThrow(() -> new EntityNotFoundException("CarModel not found"));
        Set<ComponentOptionEntity> components = dto.getRequiredComponents().stream()
                .map(id -> componentOptionJpaRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("ComponentOption not found")))
                .collect(Collectors.toSet());
        CustomAssemblyOrderEntity order = new CustomAssemblyOrderEntity();
        order.setSourceOrderId(dto.getSourceOrderId());
        order.setAssemblyOrderStatus(AssemblyOrderStatus.CREATED);
        order.setCarModel(carModelEntity);
        order.setRequiredOptions(components);
        assemblyOrderJpaRepository.save(order);
        return modelMapper.map(order, CustomAssemblyOrderResponseDto.class);
    }

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponseDto findById(UUID id) {
        AssemblyOrderEntity entity = assemblyOrderJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AssemblyOrder not found"));
        if (entity instanceof StockAssemblyOrderEntity) {
            return modelMapper.map(entity, StockAssemblyOrderResponseDto.class);
        }
        return modelMapper.map(entity, CustomAssemblyOrderResponseDto.class);
    }

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public List<AssemblyOrderResponseDto> findAll(){
        return assemblyOrderJpaRepository.findAll().stream()
                .map(e -> modelMapper.map(e, AssemblyOrderResponseDto.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void deleteById(UUID id){
        Objects.requireNonNull(id, "id cannot be null");
        AssemblyOrderEntity assemblyOrderEntity = assemblyOrderJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AssemblyOrder not found"));
        assemblyOrderEntity.setRemoved(true);
        assemblyOrderJpaRepository.save(assemblyOrderEntity);
    }

    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponseDto update(UUID id, AssemblyOrderStatus status){
        AssemblyOrderEntity entity = assemblyOrderJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AssemblyOrder not found"));
        entity.setAssemblyOrderStatus(status);
        assemblyOrderJpaRepository.save(entity);
        if (entity instanceof StockAssemblyOrderEntity) {
            return modelMapper.map(entity, StockAssemblyOrderResponseDto.class);
        }
        return modelMapper.map(entity, CustomAssemblyOrderResponseDto.class);
    }
}
