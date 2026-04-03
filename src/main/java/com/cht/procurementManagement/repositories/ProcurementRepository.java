package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.dto.procurement.ProcurementReportDTO;
import com.cht.procurementManagement.dto.procurement.SummaryReportDTO;
import com.cht.procurementManagement.entities.Procurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProcurementRepository extends JpaRepository<Procurement, Long> {
    List<Procurement> findByRequestIdIn(List<Long> requestIds);
    Optional<Procurement> findByRequestId(Long requestId);


    @Query("""
        SELECT new com.cht.procurementManagement.dto.procurement.SummaryReportDTO(
            s.name,
            CAST(p.procurementStage AS string),
            p.estimatedAmount,
            a.name
        )
        FROM Procurement p
        LEFT JOIN p.source s
        LEFT JOIN p.request r
        LEFT JOIN r.admindiv a
        WHERE p.createdOn BETWEEN :startDate AND :endDate
        ORDER BY a.name, p.procurementStage, s.name
    """)
    List<SummaryReportDTO> findSummaryReportData(
            @Param("startDate")Date startDate,
            @Param("endDate")Date endDate
            );

    @Query("""
        SELECT new com.cht.procurementManagement.dto.procurement.ProcurementReportDTO(
            p.id,
            p.name,
            p.quantity,
            p.category,
            p.method,
            p.authorityLevel,
            p.priorityStatus,
            p.commencedDate,
            s.name,
            CAST(p.procurementStage AS string),
            p.estimatedAmount,
            r.id,
            a.name
        )
        FROM Procurement p
        LEFT JOIN p.source s
        LEFT JOIN p.request r
        LEFT JOIN r.admindiv a
        WHERE p.createdOn BETWEEN :startDate AND :endDate
        ORDER BY p.procurementStage
    """)
    List<ProcurementReportDTO> findProcurementReportData(
            @Param("startDate")Date startDate,
            @Param("endDate")Date endDate
    );


    @Query("""
        SELECT new com.cht.procurementManagement.dto.procurement.ProcurementReportDTO(
            p.id,
            p.name,
            p.quantity,
            p.category,
            p.method,
            p.authorityLevel,
            p.priorityStatus,
            p.commencedDate,
            s.name,
            CAST(p.procurementStage AS string),
            p.estimatedAmount,
            r.id,
            a.name
        )
        FROM Procurement p
        LEFT JOIN p.source s
        LEFT JOIN p.request r
        LEFT JOIN r.admindiv a
        WHERE p.createdOn BETWEEN :startDate AND :endDate
        AND r.id IN :requestIds
        ORDER BY p.procurementStage
    """)
    List<ProcurementReportDTO> findDivisionProcurementReportData(
            @Param("startDate")Date startDate,
            @Param("endDate")Date endDate,
            @Param("requestIds") List<Long> requestIds
    );




}
