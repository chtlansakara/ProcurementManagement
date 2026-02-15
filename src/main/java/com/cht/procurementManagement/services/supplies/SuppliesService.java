package com.cht.procurementManagement.services.supplies;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.RequestDto;

import java.util.List;

public interface SuppliesService {

    List<RequestDto> getRequestsPendingSuppliesApproval();

    RequestDto createRequestBySupplies(RequestDto requestDto);
    List<RequestDto> getAllRequests();

    //reject a request from an admin div - create comment & change request status
    CommentDto rejectRequestBySupplies(Long requestId, CommentDto commentDto);

    //approve a request form an admin div - create approval & change request status
    ApprovalDto approveRequestBySupplies(Long requestId, ApprovalDto approvalDto);
}
