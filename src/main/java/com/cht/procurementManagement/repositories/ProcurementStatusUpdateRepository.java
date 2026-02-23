package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.ProcurementStatusUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcurementStatusUpdateRepository extends JpaRepository<ProcurementStatusUpdate, Long> {

    List<ProcurementStatusUpdate> findAllByProcurementId(Long procurementId);
}
