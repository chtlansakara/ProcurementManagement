package com.cht.procurementManagement.utils;

import com.cht.procurementManagement.entities.AuditLog;
import com.cht.procurementManagement.enums.AuditAction;
import com.cht.procurementManagement.enums.AuditEntityType;
import com.cht.procurementManagement.repositories.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    //method to create audit log
    public void log(
            String email,
            String employeeId,
            AuditAction action,
            AuditEntityType entityType,
            Long entityId,
            String description
    ){
        //create an audit log
        AuditLog auditLog = new AuditLog();
        auditLog.setEmail(email);
        auditLog.setEmployeeId(employeeId);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        //setting current time and date
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setDescription(description);
        //save to db
        auditLogRepository.save(auditLog);

    }
}
