package com.cht.procurementManagement.services.report;

import com.cht.procurementManagement.dto.RequestReportDTO;
import com.cht.procurementManagement.dto.SingleRequestReportDTO;
import com.cht.procurementManagement.entities.Approval;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.enums.ApprovalType;
import com.cht.procurementManagement.repositories.ApprovalRepository;
import com.cht.procurementManagement.repositories.RequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service

public class RequestReportService {
    private final RequestRepository requestRepository;
    private final ApprovalRepository approvalRepository;

    public RequestReportService(RequestRepository requestRepository,
                                ApprovalRepository approvalRepository) {
        this.requestRepository = requestRepository;
        this.approvalRepository = approvalRepository;
    }


    //for supplies users
    @Transactional(readOnly = true)
    public List<RequestReportDTO> getRequestReportData(Date startDate, Date endDate){
        List<RequestReportDTO> reportDTOS = requestRepository.findRequestReportData(startDate, endDate);

        //finding & adding relevant subdivisions to each request
        reportDTOS.forEach(dto -> {
            List<Subdiv> subdivs = requestRepository.findSubdivsByRequestId(dto.getId());
            if(!subdivs.isEmpty()){
                String subdivNames = subdivs.stream()
                        .map(Subdiv::getName)
                        .collect(Collectors.joining(", "));
                dto.setSubdivisions(subdivNames);
            }else{
                dto.setSubdivisions("N/A");
            }
        });

        return reportDTOS;
    }

    @Transactional(readOnly = true)
    public SingleRequestReportDTO getSingleRequestReportData(Long requestId){
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        SingleRequestReportDTO requestForReport =  SingleRequestReportDTO.builder()
                .title(request.getTitle())
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .fund(request.getFund())
                .estimation(request.getEstimation())
                .status(request.getStatus().toString())
                .previouslyPurchased(request.isPreviouslyPurchased())
                .previousPurchaseYear(request.getPreviousPurchaseYear())
                .reasonForRequirement(request.getReasonForRequirement())
                .approvedDate(request.getApprovedDate())
                .authorizedBy(request.getAuthorizedBy())
                .createdDate(request.getCreatedDate())
                .adminDivision(request.getAdmindiv().getName())
                .adminDivisionResponsible(request.getAdmindiv().getResponsibleDesignation().getTitle())
                .createdByName(request.getCreatedBy().getName())
                .createdByEmail(request.getCreatedBy().getEmail())
                .createdByDesignation(request.getCreatedBy().getDesignation().getTitle())
                .build();

        requestForReport.setSubdivNames(getSubdivNamesForRequest(requestId));
        //setup as no approvals by default
        requestForReport.setAdmindivApproved(false);
        requestForReport.setSuppliesApproved(false);

        //check approvals
        List<Approval> requestAdminApproval = approvalRepository.findAllByRequestIdAndType(requestId, ApprovalType.ADMIN_DIV);
        List<Approval> requestSuppliesApproval = approvalRepository.findAllByRequestIdAndType(requestId, ApprovalType.SUPPLIES);

        if(!requestAdminApproval.isEmpty()){
            Approval adminApproval = requestAdminApproval.get(0);
            requestForReport.setAdmindivApproved(true);
            requestForReport.setAdmindivAllocatedAmount(adminApproval.getAllocatedAmount());
            requestForReport.setAdmindivAllocatedFund(adminApproval.getFund());
            requestForReport.setAdmindivAuthorizedBy(adminApproval.getAuthroizedBy());
            requestForReport.setAdmindivApprovedDate(adminApproval.getApprovedDate());
        }
        if(!requestSuppliesApproval.isEmpty()){
            Approval suppliesApproval = requestSuppliesApproval.get(0);
            requestForReport.setSuppliesApproved(true);
            requestForReport.setSuppliesAllocatedAmount(suppliesApproval.getAllocatedAmount());
            requestForReport.setSuppliesAllocatedFund(suppliesApproval.getFund());
            requestForReport.setSuppliesAuthorizedBy(suppliesApproval.getAuthroizedBy());
            requestForReport.setSuppliesApprovedDate(suppliesApproval.getApprovedDate());
        }


        return requestForReport;
    }


    //helper method to get subdivs as a string
    private String getSubdivNamesForRequest(Long requestId){
        String subdivNames = null;
        List<Subdiv> subdivs = requestRepository.findSubdivsByRequestId(requestId);
        if(!subdivs.isEmpty()){
            subdivNames = subdivs.stream()
                    .map(Subdiv::getName)
                    .collect(Collectors.joining(", "));
        }
        return subdivNames;
    }



}
