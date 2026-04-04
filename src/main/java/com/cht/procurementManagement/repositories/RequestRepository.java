package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.dto.RequestReportDTO;
import com.cht.procurementManagement.dto.procurement.ProcurementReportDTO;
import com.cht.procurementManagement.entities.Procurement;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.services.requests.RequestService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {
    List<Request> findAll();

    Long countByIdIn(List<Long> ids);

    //sub-divs ----------------------------
    //get requests by subdiv id (can have other sub-divisions)
    @Query("SELECT DISTINCT r FROM Request r " +
            "JOIN r.subdivList s " +
            "WHERE s.id = :subdivId")
    List<Request> findAllRequestsRelatedBySubdivId(Long subdivId);


    //get requests ONLY have the subdiv id (only one sub-division)
    @Query("SELECT r FROM Request r "+
           "JOIN r.subdivList s "+
            "WHERE s.id = :subdivId AND SIZE(r.subdivList) = 1")
    List<Request> findAllRequestsOnlyBySubdivId(@Param("subdivId") Long subdivId);


    //admin -divs -------------------------------
    //get requests by subdiv id list - for admin divs list (can have other sub-divisions)
    @Query("SELECT DISTINCT r FROM Request r JOIN r.subdivList s "+
           "WHERE s.id IN :subdivIds")
    List<Request> findAllRequestsRelatedBySubdivIdList(@Param("subdivIds") List<Long> subdivIds);

    //get requests with only have sub div id list given
    @Query("""
            SELECT r FROM Request r
            WHERE NOT EXISTS (
            SELECT s FROM r.subdivList s
            WHERE s.id NOT IN :subdivIds)
            AND EXISTS(
            SELECT s FROM r.subdivList s
            WHERE s.id IN :subdivIds)
            """)
    List<Request> findAllRequestsOnlyBySubdivIdList(@Param("subdivIds") List<Long> subdivIds);

    //get prccurement with a certain requestId list
//    @Query("""
//            SELECT DISTINCT r.procurement
//            FROM Request r
//            WHERE r.id IN:requestIds
//            """)
//    List<Procurement> findAllProcurementByRequestIdList(List<Long> requestIds);

    @Query("""
        SELECT new com.cht.procurementManagement.dto.RequestReportDTO(
            r.id,
            r.title,
            r.quantity,
            r.description,
            r.fund,
            r.estimation,
            CAST(r.status AS string),
            r.approvedDate,
            r.authorizedBy,
            r.createdDate,
            a.name,
            u.email
        )
        FROM Request r
        LEFT JOIN r.admindiv a
        LEFT JOIN r.createdBy u
        WHERE r.createdDate BETWEEN :startDate AND :endDate
        ORDER BY r.createdDate
    """)
    List<RequestReportDTO> findRequestReportData(
            @Param("startDate") Date startDate,
            @Param("endDate")Date endDate
    );

    //finding subdivs of a request
    @Query("""
            SELECT s FROM Request r
            JOIN r.subdivList s
            WHERE r.id = :requestId
            """)
    List<Subdiv> findSubdivsByRequestId(@Param("requestId") Long RequestId);


    @Query("""
        SELECT new com.cht.procurementManagement.dto.RequestReportDTO(
            r.id,
            r.title,
            r.quantity,
            r.description,
            r.fund,
            r.estimation,
            CAST(r.status AS string),
            r.approvedDate,
            r.authorizedBy,
            r.createdDate,
            a.name,
            u.email
        )
        FROM Request r
        JOIN r.subdivList s
        LEFT JOIN r.admindiv a
        LEFT JOIN r.createdBy u
        WHERE s.id = :subdivId
        AND SIZE(r.subdivList) = 1
        AND r.createdDate BETWEEN :startDate AND :endDate
        ORDER BY r.createdDate
    """)
    List<RequestReportDTO> findRequestBySubdivReportData(
            @Param("subdivId") Long subdivId,
            @Param("startDate") Date startDate,
            @Param("endDate")Date endDate
    );


    @Query("""
        SELECT new com.cht.procurementManagement.dto.RequestReportDTO(
            r.id,
            r.title,
            r.quantity,
            r.description,
            r.fund,
            r.estimation,
            CAST(r.status AS string),
            r.approvedDate,
            r.authorizedBy,
            r.createdDate,
            a.name,
            u.email
        )
        FROM Request r
        LEFT JOIN r.admindiv a
        LEFT JOIN r.createdBy u
        WHERE NOT EXISTS (
        SELECT s FROM r.subdivList s
        WHERE s.id NOT IN :subdivIds)
        AND EXISTS(
        SELECT s FROM r.subdivList s
        WHERE s.id IN :subdivIds)
        AND r.createdDate BETWEEN :startDate AND :endDate
        ORDER BY r.createdDate
    """)
    List<RequestReportDTO> findRequestByAdmindivReportData(
            @Param("subdivIds") List<Long> subdivIds,
            @Param("startDate") Date startDate,
            @Param("endDate")Date endDate
    );


}
