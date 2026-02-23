package com.cht.procurementManagement.services.AuditLog;

import com.cht.procurementManagement.dto.AuditLogDto;
import com.cht.procurementManagement.entities.AuditLog;
import com.cht.procurementManagement.repositories.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogImpl implements AuditLogService{

    private final AuditLogRepository auditLogRepository;

    public AuditLogImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public List<AuditLogDto> getAuditLog() {
        return auditLogRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(AuditLog::getId).reversed())
                .map(AuditLog::getDto)
                .collect(Collectors.toList());
    }
}
