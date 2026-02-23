package com.cht.procurementManagement.services.AuditLog;

import com.cht.procurementManagement.dto.AuditLogDto;

import java.util.List;

public interface AuditLogService {
    List<AuditLogDto> getAuditLog();
}
