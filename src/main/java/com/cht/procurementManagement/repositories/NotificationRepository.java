package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.Notification;
import com.cht.procurementManagement.enums.AuditEntityType;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    long countByUserIdAndIsRead(Long userId, boolean isRead);

    @Transactional
   void deleteAllByReferenceTypeAndReferenceId(AuditEntityType type, Long referenceId);
}


