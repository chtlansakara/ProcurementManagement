package com.cht.procurementManagement.dto;

import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.enums.ApprovalType;
import com.cht.procurementManagement.enums.UserRole;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ApprovalDto {
    private Long id;
    private BigDecimal allocatedAmount;
    private String amountInWords;
    private Integer planNo;
    private String comment;
    private String fund;
    private String authorizedBy;
    private Date approvedDate;
    private Date createdDate;
    private ApprovalType type;

    //user
    private Long createdByUserId;
    private String createdByUserEmail;
    private String createdbyUsername;
    private String createdByUserEmployeeId;

    private UserRole userRoleCreatedBy;
    private String subdivCreatedBy;
    private String subdivCodeCreatedBy;
    private String admindivCreatedBy;
    private String admindivCodeCreatedBy;

    //request
    private Long requestId;
    private String requestTitle;
    private List<Long> requestSubdivIdList;
    private List<String> requestSubdivNameList;
    private List<String> requestSubdivCodeList;

    //get-set methods

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

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public String getAmountInWords() {
        return amountInWords;
    }

    public void setAmountInWords(String amountInWords) {
        this.amountInWords = amountInWords;
    }

    public Integer getPlanNo() {
        return planNo;
    }

    public void setPlanNo(Integer planNo) {
        this.planNo = planNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authroizedBy) {
        this.authorizedBy = authroizedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ApprovalType getType() {
        return type;
    }

    public void setType(ApprovalType type) {
        this.type = type;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedByUserEmail() {
        return createdByUserEmail;
    }

    public void setCreatedByUserEmail(String createdByUserEmail) {
        this.createdByUserEmail = createdByUserEmail;
    }

    public String getCreatedbyUsername() {
        return createdbyUsername;
    }

    public void setCreatedbyUsername(String createdbyUsername) {
        this.createdbyUsername = createdbyUsername;
    }

    public String getCreatedByUserEmployeeId() {
        return createdByUserEmployeeId;
    }

    public void setCreatedByUserEmployeeId(String createdByUserEmployeeId) {
        this.createdByUserEmployeeId = createdByUserEmployeeId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public void setRequestTitle(String requestTitle) {
        this.requestTitle = requestTitle;
    }

    public List<Long> getRequestSubdivIdList() {
        return requestSubdivIdList;
    }

    public void setRequestSubdivIdList(List<Long> requestSubdivIdList) {
        this.requestSubdivIdList = requestSubdivIdList;
    }

    public List<String> getRequestSubdivNameList() {
        return requestSubdivNameList;
    }

    public void setRequestSubdivNameList(List<String> requestSubdivNameList) {
        this.requestSubdivNameList = requestSubdivNameList;
    }

    public List<String> getRequestSubdivCodeList() {
        return requestSubdivCodeList;
    }

    public void setRequestSubdivCodeList(List<String> requestSubdivCodeList) {
        this.requestSubdivCodeList = requestSubdivCodeList;
    }
}
