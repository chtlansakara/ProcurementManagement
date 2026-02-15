package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String quantity;
    private String description;
    private String fund;
    private String estimation;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private boolean previouslyPurchased;
    private Integer previousPurchaseYear;
    private String reasonForRequirement;
    private Date approvedDate;
    private String authorizedBy;
    private Date createdDate;

    //User- createdBy
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "request_createdBy")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User createdBy;

    //Subdiv
    @ManyToMany()
    @JoinTable(
            name = "request_subdiv",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "subdiv_id")
    )
    @OrderColumn(name = "subdiv_order")
    private List<Subdiv> subdivList;

    //admindiv
//    @ManyToMany()
//    @JoinTable(
//            name = "request_admindiv",
//            joinColumns = @JoinColumn(name = "request_id"),
//            inverseJoinColumns = @JoinColumn(name = "admindiv_id")
//    )
//    @OrderColumn(name = "admindiv_order")
//    private List<Admindiv> admindivList;


    //to convert to request DTO
    public RequestDto getRequestDto(){
        RequestDto requestDto = new RequestDto();
        requestDto.setId(id);
        requestDto.setTitle(title);
        requestDto.setQuantity(quantity);
        requestDto.setDescription(description);
        requestDto.setFund(fund);
        requestDto.setEstimation(estimation);
        requestDto.setStatus(status);
        requestDto.setPreviouslyPurchased(previouslyPurchased);
        requestDto.setPreviousPurchaseYear(previousPurchaseYear);
        requestDto.setReasonForRequirement(reasonForRequirement);
        requestDto.setApprovedDate(approvedDate);
        requestDto.setAuthorizedBy(authorizedBy);
        requestDto.setCreatedDate(createdDate);

        //user
        if(createdBy != null){
            requestDto.setUserIdCreatedBy(createdBy.getId());
            requestDto.setEmailCreatedBy(createdBy.getEmail());
            requestDto.setUserNameCreatedBy(createdBy.getName());
            requestDto.setEmployeeIdCreatedBy(createdBy.getEmployeeId());

        }

        //sub-div
        if(subdivList != null){
            requestDto.setSubdivIdList(
                    subdivList
                            .stream()
                            .map(Subdiv::getId)
                            .toList());

            requestDto.setSubdivNameList(
                    subdivList
                            .stream()
                            .map(Subdiv::getName)
                            .toList());

            requestDto.setSubdivCodeList(
                    subdivList
                            .stream()
                            .map(Subdiv::getCode)
                            .toList());
        }

        //sub-div
//        if(admindivList != null){
//            requestDto.setAdmindivIdList(
//                    admindivList
//                            .stream()
//                            .map(Admindiv::getId)
//                            .toList());
//
//            requestDto.setAdmindivNameList(
//                    admindivList
//                            .stream()
//                            .map(Admindiv::getName)
//                            .toList());
//
//            requestDto.setAdmindivCodeList(
//                    admindivList
//                            .stream()
//                            .map(Admindiv::getCode)
//                            .toList());
//        }

        return requestDto;
    }


    //get-set methods

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


//    public List<Admindiv> getAdmindivList() {
//        return admindivList;
//    }
//
//    public void setAdmindivList(List<Admindiv> admindivList) {
//        this.admindivList = admindivList;
//    }

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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Subdiv> getSubdivList() {
        return subdivList;
    }

    public void setSubdivList(List<Subdiv> subdivList) {
        this.subdivList = subdivList;
    }
}
