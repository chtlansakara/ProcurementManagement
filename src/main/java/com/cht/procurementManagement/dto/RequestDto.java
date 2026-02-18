package com.cht.procurementManagement.dto;

import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class RequestDto {
    private Long id;
    private String title;
    private String quantity;
    private String description;
    private String fund;
    private String estimation;
    private RequestStatus status;
    private boolean previouslyPurchased;
    private Integer previousPurchaseYear;
    private String reasonForRequirement;
    private Date approvedDate;
    private String authorizedBy;
    private Date createdDate;

    //represent created by User
    private Long userIdCreatedBy;
    private String emailCreatedBy;
    private String userNameCreatedBy;
    private String employeeIdCreatedBy;

    private UserRole userRoleCreatedBy;
    private String subdivCreatedBy;
    private String subdivCodeCreatedBy;
    private String admindivCreatedBy;
    private String admindivCodeCreatedBy;


    //represent sub-division
    private List<Long> subdivIdList;
    private List<String> subdivNameList;
    private List<String> subdivCodeList;

    //represent admin-division
//    private List<Long> admindivIdList;
//    private List<String> admindivNameList;
//    private List<String> admindivCodeList;

    //get-set methods

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public UserRole getUserRoleCreatedBy() {
        return userRoleCreatedBy;
    }

    public void setUserRoleCreatedBy(UserRole userRoleCreatedBy) {
        this.userRoleCreatedBy = userRoleCreatedBy;
    }

    public String getSubdivCreatedBy() {
        return subdivCreatedBy;
    }

    public void setSubdivCreatedBy(String subdivCreatedBy) {
        this.subdivCreatedBy = subdivCreatedBy;
    }

    public String getSubdivCodeCreatedBy() {
        return subdivCodeCreatedBy;
    }

    public void setSubdivCodeCreatedBy(String subdivCodeCreatedBy) {
        this.subdivCodeCreatedBy = subdivCodeCreatedBy;
    }

    public String getAdmindivCreatedBy() {
        return admindivCreatedBy;
    }

    public void setAdmindivCreatedBy(String admindivCreatedBy) {
        this.admindivCreatedBy = admindivCreatedBy;
    }

    public String getAdmindivCodeCreatedBy() {
        return admindivCodeCreatedBy;
    }

    public void setAdmindivCodeCreatedBy(String admindivCodeCreatedBy) {
        this.admindivCodeCreatedBy = admindivCodeCreatedBy;
    }

    public Long getId() {
        return id;
    }

    public String getEmployeeIdCreatedBy() {
        return employeeIdCreatedBy;
    }

    public String getEmailCreatedBy() {
        return emailCreatedBy;
    }

    public void setEmailCreatedBy(String emailCreatedBy) {
        this.emailCreatedBy = emailCreatedBy;
    }

    public void setEmployeeIdCreatedBy(String employeeIdCreatedBy) {
        this.employeeIdCreatedBy = employeeIdCreatedBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getEstimation() {
        return estimation;
    }

    public void setEstimation(String estimation) {
        this.estimation = estimation;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public boolean isPreviouslyPurchased() {
        return previouslyPurchased;
    }

    public void setPreviouslyPurchased(boolean previouslyPurchased) {
        this.previouslyPurchased = previouslyPurchased;
    }

    public Integer getPreviousPurchaseYear() {
        return previousPurchaseYear;
    }

    public void setPreviousPurchaseYear(Integer previousPurchaseYear) {
        this.previousPurchaseYear = previousPurchaseYear;
    }

    public String getReasonForRequirement() {
        return reasonForRequirement;
    }

    public void setReasonForRequirement(String reasonForRequirement) {
        this.reasonForRequirement = reasonForRequirement;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Long getUserIdCreatedBy() {
        return userIdCreatedBy;
    }

    public void setUserIdCreatedBy(Long userIdCreatedBy) {
        this.userIdCreatedBy = userIdCreatedBy;
    }

    public String getUserNameCreatedBy() {
        return userNameCreatedBy;
    }

    public void setUserNameCreatedBy(String userNameCreatedBy) {
        this.userNameCreatedBy = userNameCreatedBy;
    }

    public List<Long> getSubdivIdList() {
        return subdivIdList;
    }

    public void setSubdivIdList(List<Long> subdivIdList) {
        this.subdivIdList = subdivIdList;
    }

    public List<String> getSubdivNameList() {
        return subdivNameList;
    }

    public void setSubdivNameList(List<String> subdivNameList) {
        this.subdivNameList = subdivNameList;
    }

    public List<String> getSubdivCodeList() {
        return subdivCodeList;
    }

    public void setSubdivCodeList(List<String> subdivCodeList) {
        this.subdivCodeList = subdivCodeList;
    }

//    public List<Long> getAdmindivIdList() {
//        return admindivIdList;
//    }

//    public void setAdmindivIdList(List<Long> admindivIdList) {
//        this.admindivIdList = admindivIdList;
//    }
//
//    public List<String> getAdmindivNameList() {
//        return admindivNameList;
//    }
//
//    public void setAdmindivNameList(List<String> admindivNameList) {
//        this.admindivNameList = admindivNameList;
//    }
//
//    public List<String> getAdmindivCodeList() {
//        return admindivCodeList;
//    }
//
//    public void setAdmindivCodeList(List<String> admindivCodeList) {
//        this.admindivCodeList = admindivCodeList;
//    }
}
