package org.example.repository.jpa;

import org.example.domain.enums.AssemblyOrderStatus;
import org.example.infrastructure.persistence.entities.assemblyOrders.AssemblyOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssemblyOrderJpaRepository extends JpaRepository<AssemblyOrderEntity, UUID> {
    List<AssemblyOrderEntity> findByAssemblyOrderStatus(AssemblyOrderStatus status);
    List<AssemblyOrderEntity> findBySourceOrderId(UUID sourceOrderId);
}
