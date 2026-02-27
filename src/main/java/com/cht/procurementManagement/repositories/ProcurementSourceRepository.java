package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.ProcurementSource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcurementSourceRepository extends JpaRepository<ProcurementSource, Long> {
    Optional<ProcurementSource> findFirstByName(String name);

}
