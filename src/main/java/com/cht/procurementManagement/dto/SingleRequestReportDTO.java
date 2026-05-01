package com.cht.procurementManagement.dto;

import com.cht.procurementManagement.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleRequestReportDTO {
    private String title;
    private String quantity;
    private String description;
    private String fund;
    private String estimation;

    private String status;
    private boolean previouslyPurchased;
    private Integer previousPurchaseYear;
    private String reasonForRequirement;
    private Date approvedDate;
    private String authorizedBy;
    private Date createdDate;

    private String adminDivision;
    private String adminDivisionResponsible;
    private String subdivNames;
    private String createdByName;
    private String createdByEmail;
    private String createdByDesignation;

    //admin approval
    private boolean admindivApproved;
    private BigDecimal admindivAllocatedAmount;
    private String admindivAllocatedFund;
    private String admindivAuthorizedBy;
    private Date admindivApprovedDate;

    //supplies approval
    private boolean suppliesApproved;
    private BigDecimal suppliesAllocatedAmount;
    private String suppliesAllocatedFund;
    private String suppliesAuthorizedBy;
    private Date suppliesApprovedDate;


}
