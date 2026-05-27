package org.example.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.exceptions.DomainValidationException;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.dto.request.CreateUserRequestDto;
import org.example.dto.response.UserResponseDto;
import org.example.infrastructure.persistence.entities.peoples.*;
import org.example.repository.jpa.UserJpaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto createUser(CreateUserRequestDto dto) {
        UserEntity entity = switch (dto.getRole()) {
            case "CLIENT" -> new ClientEntity();
            case "MANAGER" -> new ManagerEntity();
            case "WAREHOUSE_ADMIN" -> new WarehouseAdminEntity();
            case "SYSTEM_ADMIN" -> new SystemAdminEntity();
            default -> throw new DomainValidationException("Unknown role: " + dto.getRole());
        };
        entity.setName(dto.getName());
        entity.setExternalId(dto.getExternalId());
        userJpaRepository.save(entity);
        return modelMapper.map(entity, UserResponseDto.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto findById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        return userJpaRepository.findById(id)
                .map(e -> modelMapper.map(e, UserResponseDto.class))
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> findAll() {
        return userJpaRepository.findAll().stream()
                .map(e -> modelMapper.map(e, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        UserEntity entity = userJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        entity.setRemoved(true);
        userJpaRepository.save(entity);
    }

    public ManagerEntity getRandomManager() {
        List<ManagerEntity> managers = userJpaRepository.findByType(ManagerEntity.class);
        if (managers.isEmpty()) {
            throw new DomainValidationException("No managers available");
        }
        return managers.get(new Random().nextInt(managers.size()));
    }
}
