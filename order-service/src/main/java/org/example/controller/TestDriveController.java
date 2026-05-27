package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateTestDriveRequestDto;
import org.example.dto.response.TestDriveResponseDto;
import org.example.service.TestDriveService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test_drives")
public class TestDriveController {
    private final TestDriveService testDriveService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'USER')")
    public List<TestDriveResponseDto> getAll() { return testDriveService.findAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public TestDriveResponseDto getById(@PathVariable UUID id) { return testDriveService.findById(id); }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public TestDriveResponseDto create(@RequestBody CreateTestDriveRequestDto dto) { return testDriveService.create(dto); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void delete(@PathVariable UUID id) { testDriveService.deleteById(id); }
}
