package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateUserRequestDto;
import org.example.dto.response.UserResponseDto;
import org.example.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAll() { return userService.findAll(); }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto getById(@PathVariable UUID id) { return userService.findById(id); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto create(@RequestBody CreateUserRequestDto dto) { return userService.createUser(dto); }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable UUID id) { userService.deleteById(id); }
}
