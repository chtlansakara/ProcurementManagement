package com.cht.procurementManagement.services.subdiv;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.dto.procurement.ProcurementReportDTO;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.entities.Admindiv;
import com.cht.procurementManagement.entities.PDFAttachment;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.services.admindiv.AdminDivService;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SubDivService {

    //get subdiv by id
    SubdivDto getSubdiv();

    //All related requests
    List<RequestDto> getAllRequestsRelatedBySubdivId();

    //Only belong to sub-div
    List<RequestDto> getAllRequestsOnlyBySubdivId();

    RequestDto createRequestBySubdiv(RequestDto requestDto, MultipartFile file);

    //request by id
    RequestDto getRequestById(Long id);


    //get request attachment by request id
    Optional<PDFAttachment> getSubdivRequestAttachment(Long requestId);

    //get approval attachment by approval id
    Optional<PDFAttachment> getSubdivApprovalAttachment(Long approvalId);

    Resource downloadSubdivAttachment(Long fileId);

    PDFAttachment uploadRequestAttachment(MultipartFile file, Long requestId);

    void deleteRequestAttachment(Long fileId);

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
