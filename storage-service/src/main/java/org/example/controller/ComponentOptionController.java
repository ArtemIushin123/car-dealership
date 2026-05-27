package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateComponentOptionRequestDto;
import org.example.dto.response.ComponentOptionResponseDto;
import org.example.service.ComponentOptionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/component_options")
public class ComponentOptionController {
    private final ComponentOptionService componentOptionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public List<ComponentOptionResponseDto> getAll() { return componentOptionService.findAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN', 'MANAGER', 'USER')")
    public ComponentOptionResponseDto getById(@PathVariable UUID id) { return componentOptionService.findById(id); }

    @PostMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public ComponentOptionResponseDto create(@RequestBody CreateComponentOptionRequestDto dto) { return componentOptionService.create(dto); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void delete(@PathVariable UUID id) { componentOptionService.deleteById(id); }
}
