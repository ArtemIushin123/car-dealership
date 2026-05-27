package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.enums.TestDriveStatus;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.dto.request.CreateTestDriveRequestDto;
import org.example.dto.response.TestDriveResponseDto;
import org.example.infrastructure.persistence.entities.peoples.ClientEntity;
import org.example.infrastructure.persistence.entities.peoples.UserEntity;
import org.example.infrastructure.persistence.entities.testDrives.TestDriveEntity;
import org.example.repository.jpa.TestDriveJpaRepository;
import org.example.repository.jpa.UserJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TestDriveService {
    private final TestDriveJpaRepository testDriveJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public TestDriveResponseDto create(CreateTestDriveRequestDto dto) {
        UserEntity userEntity = userJpaRepository.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        TestDriveEntity entity = new TestDriveEntity();
        entity.setClient((ClientEntity) userEntity);
        entity.setStartTime(dto.getStartTime());
        entity.setTestDriveStatus(TestDriveStatus.CREATED);
        testDriveJpaRepository.save(entity);
        return modelMapper.map(entity, TestDriveResponseDto.class);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @testDriveSecurity.isOwner(#id)")
    public TestDriveResponseDto findById(UUID id) {
        return testDriveJpaRepository.findById(id)
                .map(e -> modelMapper.map(e, TestDriveResponseDto.class))
                .orElseThrow(() -> new EntityNotFoundException("TestDrive not found"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public List<TestDriveResponseDto> findAll() {
        return testDriveJpaRepository.findAll().stream()
                .map(e -> modelMapper.map(e, TestDriveResponseDto.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @orderSecurity.isOwner(#id)")
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        TestDriveEntity entity = testDriveJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TestDrive not found"));
        entity.setRemoved(true);
        testDriveJpaRepository.save(entity);
    }
}