package com.cht.procurementManagement.dto;

import com.cht.procurementManagement.enums.RequestStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Date;

public class RequestReportDTO {
    private Long id;
    private String title;
    private String quantity;
    private String description;
    private String fund;
    private String estimation;
    private String status;
    private Date approvedDate;
    private String authorizedBy;
    private Date createdDate;
    private String adminDivision;
    private String createdByEmail;
    private String subdivisions;

    public RequestReportDTO() {
    }

    public RequestReportDTO(Long id, String title, String quantity, String description, String fund, String estimation, String status, Date approvedDate, String authorizedBy, Date createdDate, String adminDivision, String createdByEmail) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.description = description;
        this.fund = fund;
        this.estimation = estimation;
        this.status = status;
        this.approvedDate = approvedDate;
        this.authorizedBy = authorizedBy;
        this.createdDate = createdDate;
        this.adminDivision = adminDivision;
        this.createdByEmail = createdByEmail;
    }

    //getter-setters


    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public Long getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getAdminDivision() {
        return adminDivision;
    }

    public void setAdminDivision(String adminDivision) {
        this.adminDivision = adminDivision;
    }

    public String getSubdivisions() {
        return subdivisions;
    }

    public void setSubdivisions(String subdivisions) {
        this.subdivisions = subdivisions;
    }
}
