package org.example.repository.jpa;

import org.example.infrastructure.persistence.entities.peoples.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    List<UserEntity> findByName(String name);
    Optional<UserEntity> findByExternalId(UUID externalId);
    @Query("SELECT u FROM UserEntity u WHERE TYPE(u) = :type")
    <T extends UserEntity> List<T> findByType(@Param("type") Class<T> type);
}
