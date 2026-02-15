package com.cht.procurementManagement.services.subdiv;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.dto.SubdivDto;
import com.cht.procurementManagement.entities.Admindiv;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.services.admindiv.AdminDivService;

import java.util.List;

public interface SubDivService {

    RequestDto createRequestBySubdiv(RequestDto requestDto);

    //All related requests
    List<RequestDto> getAllRequestsRelatedBySubdivId();

    //Only belong to sub-div
    List<RequestDto> getAllRequestsOnlyBySubdivId();

    //request by id
    RequestDto getRequestById(Long id);

    //get subdiv by id
    SubdivDto getSubdiv();


    //get comments by request id
    List<CommentDto> getCommentsByRequestId(Long id);

    //get approvals by request id
    List<ApprovalDto> getApprovalsByRequestId(Long id);


    void deleteRequestById(Long id);

    RequestDto updateRequestById(Long id, RequestDto requestDto);
}
