package com.cht.procurementManagement.services.supplies;

import com.cht.procurementManagement.dto.*;

import java.util.List;

public interface SuppliesService {
    List<RequestDto> getAllRequests();

    List<RequestDto> getRequestsPendingSuppliesApproval();

    List<RequestDto> getRequestsApproved();

    RequestDto getRequestById(Long requestId);

    RequestDto createRequestBySupplies(RequestDto requestDto);

    //reject a request from an admin div - create comment & change request status
    CommentDto rejectRequestBySupplies(Long requestId, CommentDto commentDto);

    //approve a request form an admin div - create approval & change request status
    ApprovalDto approveRequestBySupplies(Long requestId, ApprovalDto approvalDto);

    List<CommentDto> getCommentsByRequestId(Long requestId);

    List<ApprovalDto> getApprovalsByRequestId(Long requestId);

    RequestDto updateRequest(Long requestId, RequestDto requestDto);

    void deleteRequest(Long requestId);


    //get sub div list to create a request
    List<SubdivDto> getAllSubdivs();

    //get sub div list grouped by its admindiv
    List<SubdivGroupedDto> getGroupedSubdivs();
}
