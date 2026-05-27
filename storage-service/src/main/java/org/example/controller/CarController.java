package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateCarRequestDto;
import org.example.dto.response.CarResponseDto;
import org.example.service.CarService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'WAREHOUSE_ADMIN')")
    public List<CarResponseDto> getAll() {
        return carService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'WAREHOUSE_ADMIN')")
    public CarResponseDto getById(@PathVariable UUID id) {
        return carService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public CarResponseDto create(@RequestBody CreateCarRequestDto dto) {
        return carService.create(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void delete(@PathVariable UUID id) {
        carService.deleteById(id);
    }
}
