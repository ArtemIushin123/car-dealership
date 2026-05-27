package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateCarModelRequestDto;
import org.example.dto.response.CarModelResponseDto;
import org.example.service.CarModelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car_models")
public class CarModelController {
    private final CarModelService carModelService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'WAREHOUSE_ADMIN')")
    public List<CarModelResponseDto> getAll() { return carModelService.findAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MANAGER', 'WAREHOUSE_ADMIN')")
    public CarModelResponseDto getById(@PathVariable UUID id) { return carModelService.findById(id); }

    @PostMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public CarModelResponseDto create(@RequestBody CreateCarModelRequestDto dto) { return carModelService.create(dto); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void delete(@PathVariable UUID id) { carModelService.deleteById(id); }
}
