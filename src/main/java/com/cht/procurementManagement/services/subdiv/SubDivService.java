package com.cht.procurementManagement.services.subdiv;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.dto.procurement.ProcurementReportDTO;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.entities.Admindiv;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.services.admindiv.AdminDivService;

import java.util.Date;
import java.util.List;

public interface SubDivService {

    //get subdiv by id
    SubdivDto getSubdiv();

    //All related requests
    List<RequestDto> getAllRequestsRelatedBySubdivId();

    //Only belong to sub-div
    List<RequestDto> getAllRequestsOnlyBySubdivId();

    RequestDto createRequestBySubdiv(RequestDto requestDto);

    //request by id
    RequestDto getRequestById(Long id);

    void deleteRequestById(Long id);

    RequestDto updateRequestById(Long id, RequestDto requestDto);

//comment & approval related
    //get comments by request id
    List<CommentDto> getCommentsByRequestId(Long id);

    //get approvals by request id
    List<ApprovalDto> getApprovalsByRequestId(Long id);

//procurement related
    List<ProcurementResponseDto> getAllProcurementOnlyBySubdivId();

    ProcurementResponseDto getProcurementByIdForSubdiv(Long id);

    List<ProcurementReportDTO> getAllProcurementForSubdivReport(Date startDate, Date endDate);

    List<RequestReportDTO> getSubdivRequestReportData(Date startDate, Date endDate);
}
