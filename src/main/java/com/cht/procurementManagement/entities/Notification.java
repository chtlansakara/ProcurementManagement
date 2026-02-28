package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.enums.AuditEntityType;
import com.cht.procurementManagement.enums.NotificationType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private Long referenceId; //if of the request of procurement
    @Enumerated(EnumType.STRING)
    private AuditEntityType referenceType; //request or procurement
    private Long userId;
    private boolean isRead = false;
    @CreationTimestamp
    private LocalDateTime createdAt;


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

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public AuditEntityType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(AuditEntityType referenceType) {
        this.referenceType = referenceType;
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
