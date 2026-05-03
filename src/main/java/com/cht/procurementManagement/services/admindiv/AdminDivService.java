package com.cht.procurementManagement.services.admindiv;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.dto.procurement.ProcurementReportDTO;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.entities.PDFAttachment;
import com.cht.procurementManagement.entities.Request;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AdminDivService {

    //get subdiv list
    List<SubdivDto> getSubdivList();

    //all requests of admin division's sub-divs
    List<RequestDto> getAllRequestsOnlyByAdmindivId();

    //all requests that have subdivs of the admin div
    List<RequestDto> getAllRequestsRelatedByAdmindivId();

    //pending requests list
    List<RequestDto> getRequestsPendingAdmindivApproval();

    //approved requests by admin div
    List<RequestDto> getRequestsApprovedByAdmindiv();

    //rejected requests by admin div
    List<RequestDto> getRequestsRejectedByAdmindiv();

    //request by id
    RequestDto getRequestByRequestId(Long requestId);

    //get request attachment by request id
    Optional<PDFAttachment> getAdmindivRequestAttachment(Long requestId);

    //get approval attachment by approval id
    Optional<PDFAttachment> getAdmindivApprovalAttachment(Long approvalId);

    Resource downloadAdmindivAttachment(Long fileId);

    void deleteRequestAttachment(Long fileId);

    //create a request
    RequestDto createRequestByAdmindiv(RequestDto requestDto, MultipartFile file);

    //update a request
    RequestDto updateRequestByRequestId(Long requestId, RequestDto requestDto);

    //delete a request
    void deleteRequest(Long requestId);

//related to reject 0r comment requests

    //reject a request from a sub div - create comment & change request status
    CommentDto rejectRequestByAdmindiv(Long requestId, CommentDto commentDto);

    //approve a request form a sub div - create approval & change request status
    ApprovalDto approveRequestByAdmindiv(Long requestId, ApprovalDto approvalDto, MultipartFile file);

    List<CommentDto> getCommentsByRequestId(Long requestId);

    List<ApprovalDto> getApprovalsByRequestId(Long requestId);

//procurement related

    //get procurement
    List<ProcurementResponseDto> getAllProcurementOnlyByAdmindivId();

    ProcurementResponseDto getProcurementByIdForAdmindiv(Long id);

    List<ProcurementReportDTO> getAllProcurementForAdmindivReport(Date startDate, Date endDate);

    List<RequestReportDTO> getAdmindivRequestReportData(Date startDate, Date endDate);


    PDFAttachment uploadRequestAttachment(MultipartFile file, Long requestId);
}
