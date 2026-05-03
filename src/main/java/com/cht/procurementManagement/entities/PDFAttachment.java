package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.enums.EntityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PDFAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EntityType referenceType;

    private Long referenceId;
    private String name;
    private String originalName;
    private String storedPath;
    private String fileType;
    private Long fileSize;
    private LocalDateTime createdAt;
    private Long userId;
}
