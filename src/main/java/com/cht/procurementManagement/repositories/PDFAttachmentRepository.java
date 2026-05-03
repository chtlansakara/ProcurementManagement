package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.PDFAttachment;
import com.cht.procurementManagement.enums.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PDFAttachmentRepository extends JpaRepository<PDFAttachment, Long> {
    List<PDFAttachment> findAllByReferenceIdAndReferenceType(Long referenceId, EntityType referenceType);
    Optional<PDFAttachment> findFirstByReferenceIdAndReferenceType(Long referenceId, EntityType referenceType);
}
