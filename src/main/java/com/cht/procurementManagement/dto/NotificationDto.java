package com.cht.procurementManagement.dto;

import com.cht.procurementManagement.enums.AuditEntityType;
import com.cht.procurementManagement.enums.NotificationType;
import java.time.LocalDateTime;

public class NotificationDto {
    private Long id;
    private String message;
    private NotificationType type;
    private AuditEntityType referenceType; //request or procurement
    private Long referenceId; //if of the request of procurement
    private Long userId;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDto() {
    }

    public NotificationDto(Long id, String message,
                           NotificationType type,
                           AuditEntityType referenceType,
                           Long referenceId, Long userId,
                           boolean isRead,
                           LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.userId = userId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    //get-set methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public AuditEntityType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(AuditEntityType referenceType) {
        this.referenceType = referenceType;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
