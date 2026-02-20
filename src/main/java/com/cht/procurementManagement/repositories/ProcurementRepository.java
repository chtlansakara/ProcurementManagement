package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.Procurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcurementRepository extends JpaRepository<Procurement, Long> {
}
