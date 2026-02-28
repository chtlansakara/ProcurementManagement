package com.cht.procurementManagement.mappers;

import com.cht.procurementManagement.dto.NotificationDto;
import com.cht.procurementManagement.entities.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationMapper {
    public NotificationDto toDto (Notification notification){
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setReferenceId(notification.getReferenceId());
        dto.setReferenceType(notification.getReferenceType());
        dto.setUserId(notification.getUserId());
        dto.setRead(notification.getIsRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
