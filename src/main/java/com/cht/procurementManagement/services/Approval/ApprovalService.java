package com.cht.procurementManagement.services.Approval;

import com.cht.procurementManagement.dto.ApprovalDto;

import java.util.List;

public interface ApprovalService {

    ApprovalDto createApproval(ApprovalDto approvalDto);

    List<ApprovalDto> getApprovalsByRequestId(Long requestId);
}
