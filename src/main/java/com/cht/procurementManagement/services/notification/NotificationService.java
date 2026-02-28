package com.cht.procurementManagement.services.notification;

import com.cht.procurementManagement.dto.NotificationDto;
import com.cht.procurementManagement.entities.Procurement;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.enums.ApprovalType;
import com.cht.procurementManagement.enums.AuditEntityType;
import com.cht.procurementManagement.enums.ReviewType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationService {

    //methods for controller
    public List<NotificationDto> getNotificationsByUserId(Long userId);
    public void markRead(Long notificationId);
    public void markAllRead(Long userId);

    //methods for other services
    public void onRequestSubmitted(Request request);
    public void onRequestApproval(Request request, ApprovalType approvalType);
    public void onRequestRejection(Request request, ReviewType type);
    public void onProcurementCreation(Procurement procurement, Request request);
    public void onProcurementStatusChanged(Procurement procurement);

    //to delete related notifications if a request of procurement is deleted

    public void deleteNotifications(AuditEntityType type,Long entityId);

    public void deleteAllNotifications();
}
