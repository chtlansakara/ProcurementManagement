package com.cht.procurementManagement.services.AuditLog;

import com.cht.procurementManagement.dto.AuditLogDto;
import com.cht.procurementManagement.enums.AuditEntityType;

import java.util.List;

public interface AuditLogService {
    List<AuditLogDto> getAuditLog();

    void deleteAuditlogs(AuditEntityType type, Long entityId);

}
