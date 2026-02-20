package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.ProcurementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcurementStatusRepository extends JpaRepository<ProcurementStatus, Long> {
    Optional<ProcurementStatus> findFirstByName(String name);
}
