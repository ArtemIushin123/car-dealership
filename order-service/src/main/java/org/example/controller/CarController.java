package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.client.CarGrpcClient;
import org.example.dto.response.CarDto;
import org.example.grpc.CarResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarGrpcClient carGrpcClient;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public List<CarDto> getAllCars() {
        return carGrpcClient.getAllCars().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public CarDto getCarById(@PathVariable UUID id) {
        return toDto(carGrpcClient.getCarById(id));
    }

    private CarDto toDto(CarResponse car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setColor(car.getColor());
        dto.setPrice(car.getPrice());
        dto.setBodyType(car.getBodyType());
        dto.setFuelType(car.getFuelType());
        dto.setEnginePower(car.getEnginePower());
        dto.setEngineCapacity(car.getEngineCapacity());
        dto.setDriveType(car.getDriveType());
        return dto;
    }
}