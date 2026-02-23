package com.cht.procurementManagement.dto.procurement;

import com.cht.procurementManagement.enums.UserRole;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ProcurementResponseDto {
    private Long id;
    private String number;
    private String name;
    private Long quantity;
    private BigDecimal estimatedAmount;
    private String category;
    private String source;
    private String donorName;
    private String method;
    private String authorityLevel;
    private String priorityStatus;
    private String remarks;
    private String vendorDetails;
    private Date scheduledCommenceDate;
    private Date expectedCompletionDate;

    //representing objects
    //assigned to -User
    private Long assignedToUserId;
    private String assignedToUserEmail;
    private String assignedToUserName;
    private String assignedToUserEmployeeId;
    private String assignedToUserDesignation;

    //status -ProcurementStatus
    private Long statusId;
    private String statusName;

    //vendor -Vendor
    private Long vendorId;
    private String vendorName;
    private Date vendorRegisteredDate;
    private String VendorComments;

    //request list-Request List
    private List<Long> requestIdList;
    private List<String> requestTitleList;


    private Date createdOn;
    private Date lastUpdatedOn;


    //represent created by User
    private Long userIdCreatedBy;
    private String emailCreatedBy;
    private String userNameCreatedBy;
    private String employeeIdCreatedBy;
    private UserRole userRoleCreatedBy;
    private String designationCreatedBy;
    private String subdivCreatedBy;
    private String subdivCodeCreatedBy;
    private String admindivCreatedBy;
    private String admindivCodeCreatedBy;

    //represent last updated by User
    private Long userIdLastUpdatedBy;
    private String emailLastUpdatedBy;
    private String userNameLastUpdatedBy;
    private String employeeIdLastUpdatedBy;
    private UserRole userRoleLastUpdatedBy;
    private String designationUpdatedBy;

    private String subdivLastUpdatedBy;
    private String subdivCodeLastUpdatedBy;
    private String admindivLastUpdatedBy;
    private String admindivCodeLastUpdatedBy;


    //get-set methods


    public String getDesignationCreatedBy() {
        return designationCreatedBy;
    }

    public void setDesignationCreatedBy(String designationCreatedBy) {
        this.designationCreatedBy = designationCreatedBy;
    }

    public String getDesignationUpdatedBy() {
        return designationUpdatedBy;
    }

    public void setDesignationUpdatedBy(String designationUpdatedBy) {
        this.designationUpdatedBy = designationUpdatedBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
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

    public Long getUserIdLastUpdatedBy() {
        return userIdLastUpdatedBy;
    }

    public void setUserIdLastUpdatedBy(Long userIdLastUpdatedBy) {
        this.userIdLastUpdatedBy = userIdLastUpdatedBy;
    }

    public String getEmailLastUpdatedBy() {
        return emailLastUpdatedBy;
    }

    public void setEmailLastUpdatedBy(String emailLastUpdatedBy) {
        this.emailLastUpdatedBy = emailLastUpdatedBy;
    }

    public String getUserNameLastUpdatedBy() {
        return userNameLastUpdatedBy;
    }

    public void setUserNameLastUpdatedBy(String userNameLastUpdatedBy) {
        this.userNameLastUpdatedBy = userNameLastUpdatedBy;
    }

    public String getEmployeeIdLastUpdatedBy() {
        return employeeIdLastUpdatedBy;
    }

    public void setEmployeeIdLastUpdatedBy(String employeeIdLastUpdatedBy) {
        this.employeeIdLastUpdatedBy = employeeIdLastUpdatedBy;
    }

    public UserRole getUserRoleLastUpdatedBy() {
        return userRoleLastUpdatedBy;
    }

    public void setUserRoleLastUpdatedBy(UserRole userRoleLastUpdatedBy) {
        this.userRoleLastUpdatedBy = userRoleLastUpdatedBy;
    }

    public String getSubdivLastUpdatedBy() {
        return subdivLastUpdatedBy;
    }

    public void setSubdivLastUpdatedBy(String subdivLastUpdatedBy) {
        this.subdivLastUpdatedBy = subdivLastUpdatedBy;
    }

    public String getSubdivCodeLastUpdatedBy() {
        return subdivCodeLastUpdatedBy;
    }

    public void setSubdivCodeLastUpdatedBy(String subdivCodeLastUpdatedBy) {
        this.subdivCodeLastUpdatedBy = subdivCodeLastUpdatedBy;
    }

    public String getAdmindivLastUpdatedBy() {
        return admindivLastUpdatedBy;
    }

    public void setAdmindivLastUpdatedBy(String admindivLastUpdatedBy) {
        this.admindivLastUpdatedBy = admindivLastUpdatedBy;
    }

    public String getAdmindivCodeLastUpdatedBy() {
        return admindivCodeLastUpdatedBy;
    }

    public void setAdmindivCodeLastUpdatedBy(String admindivCodeLastUpdatedBy) {
        this.admindivCodeLastUpdatedBy = admindivCodeLastUpdatedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(BigDecimal estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAuthorityLevel() {
        return authorityLevel;
    }

    public void setAuthorityLevel(String authorityLevel) {
        this.authorityLevel = authorityLevel;
    }

    public String getPriorityStatus() {
        return priorityStatus;
    }

    public void setPriorityStatus(String priorityStatus) {
        this.priorityStatus = priorityStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getVendorDetails() {
        return vendorDetails;
    }

    public void setVendorDetails(String vendorDetails) {
        this.vendorDetails = vendorDetails;
    }

    public Date getScheduledCommenceDate() {
        return scheduledCommenceDate;
    }

    public void setScheduledCommenceDate(Date scheduledCommenceDate) {
        this.scheduledCommenceDate = scheduledCommenceDate;
    }

    public Date getExpectedCompletionDate() {
        return expectedCompletionDate;
    }

    public void setExpectedCompletionDate(Date expectedCompletionDate) {
        this.expectedCompletionDate = expectedCompletionDate;
    }

    public Long getAssignedToUserId() {
        return assignedToUserId;
    }

    public void setAssignedToUserId(Long assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }

    public String getAssignedToUserEmail() {
        return assignedToUserEmail;
    }

    public void setAssignedToUserEmail(String assignedToUserEmail) {
        this.assignedToUserEmail = assignedToUserEmail;
    }

    public String getAssignedToUserName() {
        return assignedToUserName;
    }

    public void setAssignedToUserName(String assignedToUserName) {
        this.assignedToUserName = assignedToUserName;
    }

    public String getAssignedToUserEmployeeId() {
        return assignedToUserEmployeeId;
    }

    public void setAssignedToUserEmployeeId(String assignedToUserEmployeeId) {
        this.assignedToUserEmployeeId = assignedToUserEmployeeId;
    }

    public String getAssignedToUserDesignation() {
        return assignedToUserDesignation;
    }

    public void setAssignedToUserDesignation(String assignedToUserDesignation) {
        this.assignedToUserDesignation = assignedToUserDesignation;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Date getVendorRegisteredDate() {
        return vendorRegisteredDate;
    }

    public void setVendorRegisteredDate(Date vendorRegisteredDate) {
        this.vendorRegisteredDate = vendorRegisteredDate;
    }

    public String getVendorComments() {
        return VendorComments;
    }

    public void setVendorComments(String vendorComments) {
        VendorComments = vendorComments;
    }

    public List<Long> getRequestIdList() {
        return requestIdList;
    }

    public void setRequestIdList(List<Long> requestIdList) {
        this.requestIdList = requestIdList;
    }

    public List<String> getRequestTitleList() {
        return requestTitleList;
    }

    public void setRequestTitleList(List<String> requestTitleList) {
        this.requestTitleList = requestTitleList;
    }


}
