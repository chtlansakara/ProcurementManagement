package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.services.requests.RequestService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}
