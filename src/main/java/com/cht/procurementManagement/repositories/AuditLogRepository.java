package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.AuditLog;
import com.cht.procurementManagement.enums.AuditEntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Transactional
    void deleteAllByEntityTypeAndEntityId(AuditEntityType entityType,Long entityId);

}
