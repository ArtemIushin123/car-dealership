package org.example.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.enums.CarStatus;
import org.example.domain.exceptions.DomainValidationException;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.dto.request.CreateCarRequestDto;
import org.example.dto.response.CarResponseDto;
import org.example.infrastructure.persistence.entities.cars.CarEntity;
import org.example.infrastructure.persistence.entities.cars.CarModelEntity;
import org.example.repository.jpa.CarJpaRepository;
import org.example.repository.jpa.CarModelJpaRepository;
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
public class CarService {
    private final CarJpaRepository carJpaRepository;
    private final CarModelJpaRepository carModelJpaRepository;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN')")
    public CarResponseDto create(CreateCarRequestDto dto) {
        validateColor(dto.getColor());
        CarModelEntity carModelEntity = carModelJpaRepository.findById(dto.getCarModelId())
                .orElseThrow(() -> new EntityNotFoundException("CarModel not found"));
        CarEntity entity = new CarEntity();
        entity.setCarModel(carModelEntity);
        entity.setColor(dto.getColor());
        entity.setStatus(CarStatus.AVAILABLE);
        carJpaRepository.save(entity);
        return modelMapper.map(entity, CarResponseDto.class);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN', 'MANAGER', 'USER')")
    public CarResponseDto findById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        return carJpaRepository.findById(id)
                .map(e -> modelMapper.map(e, CarResponseDto.class))
                .orElseThrow(() -> new EntityNotFoundException("Car with id " + id + " does not exist"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN', 'MANAGER', 'USER')")
    public List<CarResponseDto> findAll() {
        return carJpaRepository.findAll().stream()
                .map(e -> modelMapper.map(e, CarResponseDto.class))
                .collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN', 'MANAGER', 'USER')")
    public List<CarResponseDto> findAvailableCars() {
        return carJpaRepository.findByStatus(CarStatus.AVAILABLE).stream()
                .map(e -> modelMapper.map(e, CarResponseDto.class))
                .collect(Collectors.toList());
    }

    public void markAvailable(UUID id) {
        CarEntity car = carJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        car.setStatus(CarStatus.AVAILABLE);
        carJpaRepository.save(car);
    }

    public void markReserved(UUID id) {
        CarEntity car = carJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        car.setStatus(CarStatus.RESERVED);
        carJpaRepository.save(car);
    }

    public void markForTestDrive(UUID id) {
        CarEntity car = carJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        car.setStatus(CarStatus.TEST_DRIVE);
        carJpaRepository.save(car);
    }

    public void markSold(UUID id) {
        CarEntity car = carJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        car.setStatus(CarStatus.SOLD);
        carJpaRepository.save(car);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN')")
    public void deleteById(UUID id) {
        CarEntity car = carJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car not found"));
        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new DomainValidationException("Only available cars can be deleted");
        }
        car.setRemoved(true);
        carJpaRepository.save(car);
    }

    private void validateColor(String color) {
        Objects.requireNonNull(color, "color cannot be null");
        if (color.isBlank()) throw new DomainValidationException("color cannot be blank");
    }
}
