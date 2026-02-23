package com.cht.procurementManagement.mappers;

import com.cht.procurementManagement.dto.procurement.ProcurementCreateDto;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.entities.Procurement;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.entities.User;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProcurementMapper {

    //method to convert Procurement -> ProcurementResponseDto
    public ProcurementResponseDto toResponseDto(Procurement procurement) {
        ProcurementResponseDto responseDto = new ProcurementResponseDto();

        responseDto.setId(procurement.getId());
        responseDto.setNumber(procurement.getNumber());
        responseDto.setName(procurement.getName());
        responseDto.setQuantity(procurement.getQuantity());
        responseDto.setEstimatedAmount(procurement.getEstimatedAmount());
        responseDto.setCategory(procurement.getCategory());
        responseDto.setSource(procurement.getSource());
        responseDto.setDonorName(procurement.getDonorName());
        responseDto.setMethod(procurement.getMethod());
        responseDto.setAuthorityLevel(procurement.getAuthorityLevel());
        responseDto.setPriorityStatus(procurement.getPriorityStatus());
        responseDto.setRemarks(procurement.getRemarks());
        responseDto.setVendorDetails(procurement.getVendorDetails());
        responseDto.setScheduledCommenceDate(procurement.getScheduledCommenceDate());
        responseDto.setExpectedCompletionDate(procurement.getExpectedCompletionDate());
        responseDto.setCreatedOn(procurement.getCreatedOn());
        responseDto.setLastUpdatedOn(procurement.getLastUpdatedOn());
        //for objects
        //assigned to
        if (procurement.getAssignedTo() != null) {
            responseDto.setAssignedToUserId(procurement.getAssignedTo().getId());
            responseDto.setAssignedToUserEmail(procurement.getAssignedTo().getEmail());
            responseDto.setAssignedToUserName(procurement.getAssignedTo().getName());
            responseDto.setAssignedToUserEmployeeId(procurement.getAssignedTo().getEmployeeId());
            if (procurement.getAssignedTo().getDesignation() != null) {
                responseDto.setAssignedToUserDesignation(procurement.getAssignedTo().getDesignation().getCode());
            }
        }
        //status
        if (procurement.getStatus() != null) {
            responseDto.setStatusId(procurement.getStatus().getId());
            responseDto.setStatusName(procurement.getStatus().getName());
        }
        //vendor
        if (procurement.getVendor() != null) {
            responseDto.setVendorId(procurement.getVendor().getId());
            responseDto.setVendorName(procurement.getVendor().getName());
            responseDto.setVendorRegisteredDate(procurement.getVendor().getRegisteredDate());
            responseDto.setVendorComments(procurement.getVendor().getComments());
        }
        //request list
        if (procurement.getRequestList() != null) {
            responseDto.setRequestIdList(procurement.getRequestList()
                    .stream()
                    .map(Request::getId)
                    .collect(Collectors.toList()));

            responseDto.setRequestTitleList(procurement.getRequestList()
                    .stream()
                    .map(Request::getTitle)
                    .collect(Collectors.toList()));
        }
        if(procurement.getCreatedBy() != null){
            responseDto.setUserIdCreatedBy(procurement.getCreatedBy().getId());
            responseDto.setEmailCreatedBy(procurement.getCreatedBy().getEmail());
            responseDto.setUserNameCreatedBy(procurement.getCreatedBy().getName());
            responseDto.setEmployeeIdCreatedBy(procurement.getCreatedBy().getEmployeeId());
            responseDto.setUserRoleCreatedBy(procurement.getCreatedBy().getUserRole());
            responseDto.setDesignationCreatedBy(procurement.getCreatedBy().getDesignation().getCode());

            responseDto.setSubdivCreatedBy(procurement.getCreatedBy().getSubdiv().getName());
            responseDto.setSubdivCodeCreatedBy(procurement.getCreatedBy().getSubdiv().getCode());
            responseDto.setAdmindivCreatedBy(procurement.getCreatedBy().getAdmindiv().getName());
            responseDto.setAdmindivCodeCreatedBy(procurement.getCreatedBy().getAdmindiv().getCode());
        }

        //user
        if(procurement.getLastUpdatedBy() != null){
            responseDto.setUserIdLastUpdatedBy(procurement.getLastUpdatedBy().getId());
            responseDto.setEmailLastUpdatedBy(procurement.getLastUpdatedBy().getEmail());
            responseDto.setUserNameLastUpdatedBy(procurement.getLastUpdatedBy().getName());
            responseDto.setEmployeeIdLastUpdatedBy(procurement.getLastUpdatedBy().getEmployeeId());
            responseDto.setUserRoleLastUpdatedBy(procurement.getLastUpdatedBy().getUserRole());
            responseDto.setDesignationUpdatedBy(procurement.getLastUpdatedBy().getDesignation().getCode());


            responseDto.setSubdivLastUpdatedBy(procurement.getLastUpdatedBy().getSubdiv().getName());
            responseDto.setSubdivCodeLastUpdatedBy(procurement.getLastUpdatedBy().getSubdiv().getCode());
            responseDto.setAdmindivLastUpdatedBy(procurement.getLastUpdatedBy().getAdmindiv().getName());
            responseDto.setAdmindivCodeLastUpdatedBy(procurement.getLastUpdatedBy().getAdmindiv().getCode());
        }
        return responseDto;
    }


    public Procurement dtoToProcurement(ProcurementCreateDto dto) {
        Procurement procurement = new Procurement();

        procurement.setNumber(dto.getNumber());
        procurement.setName(dto.getName());
        procurement.setQuantity(dto.getQuantity());
        procurement.setEstimatedAmount(dto.getEstimatedAmount());
        procurement.setCategory(dto.getCategory());
        procurement.setSource(dto.getSource());
        procurement.setDonorName(dto.getDonorName());
        procurement.setMethod(dto.getMethod());
        procurement.setAuthorityLevel(dto.getAuthorityLevel());
        procurement.setPriorityStatus(dto.getPriorityStatus());
        procurement.setRemarks(dto.getRemarks());
        procurement.setVendorDetails(dto.getVendorDetails());
        procurement.setScheduledCommenceDate(dto.getScheduledCommenceDate());
        procurement.setExpectedCompletionDate(dto.getExpectedCompletionDate());

        return procurement;
    }


    public List<ProcurementResponseDto> toResponseDtoList(List<Procurement> procurements) {
        return procurements
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public Procurement updateProcurementWithDto(Procurement procurement, ProcurementCreateDto dto) {
        procurement.setNumber(dto.getNumber());
        procurement.setName(dto.getName());
        procurement.setQuantity(dto.getQuantity());
        procurement.setEstimatedAmount(dto.getEstimatedAmount());
        procurement.setCategory(dto.getCategory());
        procurement.setSource(dto.getSource());
        procurement.setDonorName(dto.getDonorName());
        procurement.setMethod(dto.getMethod());
        procurement.setAuthorityLevel(dto.getAuthorityLevel());
        procurement.setPriorityStatus(dto.getPriorityStatus());
        procurement.setRemarks(dto.getRemarks());
        procurement.setVendorDetails(dto.getVendorDetails());
        procurement.setScheduledCommenceDate(dto.getScheduledCommenceDate());
        procurement.setExpectedCompletionDate(dto.getExpectedCompletionDate());

        return procurement;
    }


}
