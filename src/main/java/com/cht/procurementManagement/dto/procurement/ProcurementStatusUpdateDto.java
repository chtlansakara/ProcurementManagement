package com.cht.procurementManagement.dto.procurement;

import com.cht.procurementManagement.enums.UserRole;

import java.util.Date;

public class ProcurementStatusUpdateDto {
    private Long id;
    private String comment;
    private String procurementStage;



    private Date statusChangedOn;
    private Date createdOn;


    //representing related ProcurementStatus
    private Long procurementStatusId;
    private String procurementStatusName;

    //representing related Procurement
    private Long procurementId;
    private String procurementName;
    private Date procurementCreatedOn;


    private Long procurementAssignedToUserId;
    private String procurementAssignedToUserEmail;
    private String procurementAssignedToDesignation;
    private String procurementAssignedToName;


    //representing related created by User
    private Long userIdCreatedBy;
    private String emailCreatedBy;
    private String userNameCreatedBy;
    private String employeeIdCreatedBy;
    private UserRole userRoleCreatedBy;
    private String designationCreatedBy;

    //get-set methods


    public String getProcurementStage() {
        return procurementStage;
    }

    public void setProcurementStage(String procurementStage) {
        this.procurementStage = procurementStage;
    }

    public Date getStatusChangedOn() {
        return statusChangedOn;
    }

    public void setStatusChangedOn(Date statusChangedOn) {
        this.statusChangedOn = statusChangedOn;
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getProcurementStatusId() {
        return procurementStatusId;
    }

    public void setProcurementStatusId(Long procurementStatusId) {
        this.procurementStatusId = procurementStatusId;
    }

    public String getProcurementStatusName() {
        return procurementStatusName;
    }

    public void setProcurementStatusName(String procurementStatusName) {
        this.procurementStatusName = procurementStatusName;
    }

    public Long getProcurementId() {
        return procurementId;
    }

    public void setProcurementId(Long procurementId) {
        this.procurementId = procurementId;
    }

    public String getProcurementName() {
        return procurementName;
    }

    public void setProcurementName(String procurementName) {
        this.procurementName = procurementName;
    }

    public Date getProcurementCreatedOn() {
        return procurementCreatedOn;
    }

    public void setProcurementCreatedOn(Date procurementCreatedOn) {
        this.procurementCreatedOn = procurementCreatedOn;
    }

    public Long getProcurementAssignedToUserId() {
        return procurementAssignedToUserId;
    }

    public void setProcurementAssignedToUserId(Long procurementAssignedToUserId) {
        this.procurementAssignedToUserId = procurementAssignedToUserId;
    }

    public String getProcurementAssignedToUserEmail() {
        return procurementAssignedToUserEmail;
    }

    public void setProcurementAssignedToUserEmail(String procurementAssignedToUserEmail) {
        this.procurementAssignedToUserEmail = procurementAssignedToUserEmail;
    }

    public String getProcurementAssignedToDesignation() {
        return procurementAssignedToDesignation;
    }

    public void setProcurementAssignedToDesignation(String procurementAssignedToDesignation) {
        this.procurementAssignedToDesignation = procurementAssignedToDesignation;
    }

    public String getProcurementAssignedToName() {
        return procurementAssignedToName;
    }

    public void setProcurementAssignedToName(String procurementAssignedToName) {
        this.procurementAssignedToName = procurementAssignedToName;
    }

    public Long getUserIdCreatedBy() {
        return userIdCreatedBy;
    }

    public void setUserIdCreatedBy(Long userIdCreatedBy) {
        this.userIdCreatedBy = userIdCreatedBy;
    }

    public String getEmailCreatedBy() {
        return emailCreatedBy;
    }

    public void setEmailCreatedBy(String emailCreatedBy) {
        this.emailCreatedBy = emailCreatedBy;
    }

    public String getUserNameCreatedBy() {
        return userNameCreatedBy;
    }

    public void setUserNameCreatedBy(String userNameCreatedBy) {
        this.userNameCreatedBy = userNameCreatedBy;
    }

    public String getEmployeeIdCreatedBy() {
        return employeeIdCreatedBy;
    }

    public void setEmployeeIdCreatedBy(String employeeIdCreatedBy) {
        this.employeeIdCreatedBy = employeeIdCreatedBy;
    }

    public UserRole getUserRoleCreatedBy() {
        return userRoleCreatedBy;
    }

    public void setUserRoleCreatedBy(UserRole userRoleCreatedBy) {
        this.userRoleCreatedBy = userRoleCreatedBy;
    }

    public String getDesignationCreatedBy() {
        return designationCreatedBy;
    }

    public void setDesignationCreatedBy(String designationCreatedBy) {
        this.designationCreatedBy = designationCreatedBy;
    }
}
