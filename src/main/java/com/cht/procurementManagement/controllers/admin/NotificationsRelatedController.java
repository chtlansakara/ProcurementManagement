package com.cht.procurementManagement.controllers.admin;

import com.cht.procurementManagement.services.notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class NotificationsRelatedController {

    private final NotificationService notificationService;

    public NotificationsRelatedController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @DeleteMapping("/notifications-delete")
    public ResponseEntity<Void> deleteAllNotifications() {
        notificationService.deleteAllNotifications();
        return ResponseEntity.ok(null);
    }
}
