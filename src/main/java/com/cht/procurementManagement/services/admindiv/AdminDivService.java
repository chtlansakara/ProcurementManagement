package com.cht.procurementManagement.services.admindiv;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.dto.SubdivDto;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.entities.Request;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminDivService {

    //all requests of admin div's sub divisions
    List<RequestDto> getAllRequestsOnlyByAdmindivId();

    //request by id
    RequestDto getRequestByRequestId(Long requestId);

    //all requests that have subdivs of the admin div
    List<RequestDto> getAllRequestsRelatedByAdmindivId();

    //pending requests list
    List<RequestDto> getRequestsPendingAdmindivApproval();

    //approved requests by admin div
    List<RequestDto> getRequestsApprovedByAdmindiv();
    //rejected requests by admin div
    List<RequestDto> getRequestsRejectedByAdmindiv();

    //create a request
    RequestDto createRequestByAdmindiv(RequestDto requestDto);

    //update a request
    RequestDto updateRequestByRequestId(Long requestId, RequestDto requestDto);

    //delete a request
    void deleteRequest(Long requestId);

    //reject a request from a sub div - create comment & change request status
    CommentDto rejectRequestByAdmindiv(Long requestId, CommentDto commentDto);

    List<CommentDto> getCommentsByRequestId(Long requestId);

    List<ApprovalDto> getApprovalsByRequestId(Long requestId);

    //approve a request form a sub div - create approval & change request status
    ApprovalDto approveRequestByAdmindiv(Long requestId, ApprovalDto approvalDto);

    //get subdiv list
    List<SubdivDto> getSubdivList();

    //get procurement
    List<ProcurementResponseDto> getAllProcurementOnlyByAdmindivId();
    ProcurementResponseDto getProcurementByIdForAdmindiv(Long id);


}
