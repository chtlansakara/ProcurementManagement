package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.procurement.ProcurementStatusUpdateDto;
import com.cht.procurementManagement.enums.ProcurementStage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class ProcurementStatusUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private Date statusChangedOn;
    private Date createdOn;
    @Enumerated(EnumType.STRING)
    private ProcurementStage procurementStage;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    @JsonIgnore
    private ProcurementStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "procurement_id", nullable = false)
    @JsonIgnore
    private Procurement procurement;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "createdBy_id", nullable = false)
    @JsonIgnore
    private User createdBy;

    //method to get dto
    public ProcurementStatusUpdateDto getDto() {
        ProcurementStatusUpdateDto dto = new ProcurementStatusUpdateDto();
        dto.setId(id);
        dto.setComment(comment);
        dto.setStatusChangedOn(statusChangedOn);
        dto.setCreatedOn(createdOn);
        dto.setProcurementStage(procurementStage.toString());

        if (status != null) {
            dto.setProcurementStatusId(status.getId());
            dto.setProcurementStatusName(status.getName());
        }
        if (procurement != null) {
            dto.setProcurementId(procurement.getId());
            dto.setProcurementName(procurement.getName());
            dto.setProcurementCreatedOn(procurement.getCreatedOn());

            dto.setProcurementAssignedToUserId(procurement.getAssignedTo().getId());
            dto.setProcurementAssignedToUserEmail(procurement.getAssignedTo().getEmail());
            dto.setProcurementAssignedToDesignation(procurement.getAssignedTo().getDesignation().getCode());
            dto.setProcurementAssignedToName(procurement.getAssignedTo().getName());
        }
        if (createdBy != null) {
            dto.setUserIdCreatedBy(createdBy.getId());
            dto.setEmailCreatedBy(createdBy.getEmail());
            dto.setUserNameCreatedBy(createdBy.getName());
            dto.setEmployeeIdCreatedBy(createdBy.getEmployeeId());
            dto.setUserRoleCreatedBy(createdBy.getUserRole());
            dto.setDesignationCreatedBy(createdBy.getDesignation().getCode());
        }

        return dto;
    }


    //get-set methods

    public ProcurementStage getProcurementStage() {
        return procurementStage;
    }

    public void setProcurementStage(ProcurementStage procurementStage) {
        this.procurementStage = procurementStage;
    }

    public Date getStatusChangedOn() {
        return statusChangedOn;
    }

    public void setStatusChangedOn(Date statusChangedOn) {
        this.statusChangedOn = statusChangedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ProcurementStatus getStatus() {
        return status;
    }

    public void setStatus(ProcurementStatus status) {
        this.status = status;
    }

    public Procurement getProcurement() {
        return procurement;
    }

    public void setProcurement(Procurement procurement) {
        this.procurement = procurement;
    }
}
