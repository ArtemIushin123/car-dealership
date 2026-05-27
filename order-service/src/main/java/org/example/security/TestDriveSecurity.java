package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.infrastructure.persistence.entities.testDrives.TestDriveEntity;
import org.example.repository.jpa.TestDriveJpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("testDriveSecurity")
@RequiredArgsConstructor
public class TestDriveSecurity {
    private final TestDriveJpaRepository testDriveJpaRepository;
    public boolean isOwner(UUID orderId) {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication();
        String subject = auth.getToken().getSubject();
        TestDriveEntity testDriveEntity = testDriveJpaRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("Test Drive not found"));
        UUID externalId = testDriveEntity.getClient().getExternalId();
        if (externalId == null) {
            return false;
        }
        return subject.equals(externalId.toString());
    }
}
