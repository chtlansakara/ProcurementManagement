package com.cht.procurementManagement.services.admindiv;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.entities.Request;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdminDivService {
    List<RequestDto> getAllRequestsOnlyByAdmindivId();


    List<RequestDto> getRequestsPendingAdmindivApproval();

    List<RequestDto> getAllRequestsRelatedByAdmindivId();

    RequestDto createRequestByAdmindiv(RequestDto requestDto);

    //reject a request from a sub div - create comment & change request status
    CommentDto rejectRequestByAdmindiv(Long requestId, CommentDto commentDto);

    //approve a request form a sub div - create approval & change request status
    ApprovalDto approveRequestByAdmindiv(Long requestId, ApprovalDto approvalDto);
}
