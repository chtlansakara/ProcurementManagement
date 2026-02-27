package com.cht.procurementManagement.dto.procurement;

import java.math.BigDecimal;

public class SummaryReportDTO {
    private String sourceName;
    private String procurementStage;
    private BigDecimal estimatedAmount;
    private String adminDivision;

    public SummaryReportDTO(){}

    public SummaryReportDTO(String sourceName, String procurementStage, BigDecimal estimatedAmount, String adminDivision) {
        this.sourceName = sourceName;
        this.procurementStage = procurementStage;
        this.estimatedAmount = estimatedAmount;
        this.adminDivision = adminDivision;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getProcurementStage() {
        return procurementStage;
    }

    public void setProcurementStage(String procurementStage) {
        this.procurementStage = procurementStage;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public String getAdminDivision() {
        return adminDivision;
    }

    public void setAdminDivision(String adminDivision) {
        this.adminDivision = adminDivision;
    }
}
