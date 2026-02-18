package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.enums.ApprovalType;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal allocatedAmount;
    private String amountInWords;
    private Integer planNo;
    private String comment;
    private String fund;
    private String authorizedBy;
    private Date approvedDate;
    private Date createdDate;
    @Enumerated(EnumType.STRING)
    private ApprovalType type;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Request request;

    //get approval-dto method
    public ApprovalDto getApprovalDto(){
        ApprovalDto approvalDto = new ApprovalDto();
        approvalDto.setId(id);
        approvalDto.setAllocatedAmount(allocatedAmount);
        approvalDto.setAmountInWords(amountInWords);
        approvalDto.setPlanNo(planNo);
        approvalDto.setComment(comment);
        approvalDto.setFund(fund);
        approvalDto.setAuthorizedBy(authorizedBy);
        approvalDto.setApprovedDate(approvedDate);
        approvalDto.setCreatedDate(createdDate);
        approvalDto.setType(type);
        //user details
        if(createdBy!= null){
            approvalDto.setCreatedByUserId(createdBy.getId());
            approvalDto.setCreatedByUserEmail(createdBy.getEmail());
            approvalDto.setCreatedbyUsername(createdBy.getName());
            approvalDto.setCreatedByUserEmployeeId(createdBy.getEmployeeId());
            approvalDto.setUserRoleCreatedBy(createdBy.getUserRole());
            approvalDto.setSubdivCreatedBy(createdBy.getSubdiv().getName());
            approvalDto.setSubdivCodeCreatedBy(createdBy.getSubdiv().getCode());
            approvalDto.setAdmindivCreatedBy(createdBy.getAdmindiv().getName());
            approvalDto.setAdmindivCodeCreatedBy(createdBy.getAdmindiv().getCode());
        }
        //request details
        if(request!= null){
            approvalDto.setRequestId(request.getId());
            approvalDto.setRequestTitle(request.getTitle());
            approvalDto.setRequestSubdivIdList(
                    request.getSubdivList()
                            .stream()
                            .map(Subdiv::getId)
                            .toList());
            approvalDto.setRequestSubdivNameList(
                    request.getSubdivList()
                            .stream()
                            .map(Subdiv::getName)
                            .toList());
            approvalDto.setRequestSubdivCodeList(
                    request.getSubdivList()
                            .stream()
                            .map(Subdiv::getCode)
                            .toList());
        }
        return approvalDto;
    }

    //get-set methods


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

    public String getAuthroizedBy() {
        return authorizedBy;
    }

    public void setAuthroizedBy(String authroizedBy) {
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
