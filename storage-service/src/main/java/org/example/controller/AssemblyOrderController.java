package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.enums.AssemblyOrderStatus;
import org.example.dto.request.CreateCustomAssemblyOrderRequestDto;
import org.example.dto.request.CreateStockAssemblyOrderRequestDto;
import org.example.dto.response.AssemblyOrderResponseDto;
import org.example.dto.response.CustomAssemblyOrderResponseDto;
import org.example.dto.response.StockAssemblyOrderResponseDto;
import org.example.service.AssemblyOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assembly_order")
public class AssemblyOrderController {
    private final AssemblyOrderService assemblyOrderService;

    @PostMapping("/stock")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public StockAssemblyOrderResponseDto createStockAssemblyOrder(@RequestBody CreateStockAssemblyOrderRequestDto dto){
        return assemblyOrderService.createStockAssemblyOrder(dto);
    }

    @PostMapping("/custom")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public CustomAssemblyOrderResponseDto createCustomAssemblyOrder(@RequestBody CreateCustomAssemblyOrderRequestDto dto){
        return assemblyOrderService.createCustomAssemblyOrder(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponseDto getById(@PathVariable UUID id){
        return assemblyOrderService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public List<AssemblyOrderResponseDto> getAll(){
        return assemblyOrderService.findAll();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void delete(@PathVariable UUID id){
        assemblyOrderService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public AssemblyOrderResponseDto update(@PathVariable UUID id, @RequestParam AssemblyOrderStatus status){
        return assemblyOrderService.update(id, status);
    }
}
