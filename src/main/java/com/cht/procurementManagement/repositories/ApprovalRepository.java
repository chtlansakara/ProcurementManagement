package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval,Long> {
    List<Approval> findAllByRequestId(Long requestId);
}
