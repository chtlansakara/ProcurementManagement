package com.cht.procurementManagement.services.supplies;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.entities.PDFAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SuppliesService {

    //Requests related   -----------------------------------------------------

    List<RequestDto> getAllRequests();

    List<RequestDto> getRequestsPendingSuppliesApproval();

    List<RequestDto> getRequestsApproved();

    RequestDto getRequestById(Long requestId);

    RequestDto createRequestBySupplies(RequestDto requestDto, MultipartFile file) throws IOException;

    RequestDto updateRequest(Long requestId, RequestDto requestDto);

    //update request attachment
    PDFAttachment uploadRequestAttachment(MultipartFile file, Long requestId) throws IOException;

    //delete request attachment
    void deleteRequestAttachment(Long fileId) throws IOException;

    void deleteRequest(Long requestId);

    //reject a request from an admin div - create comment & change request status
    CommentDto rejectRequestBySupplies(Long requestId, CommentDto commentDto);

    //approve a request form an admin div - create approval & change request status
    ApprovalDto approveRequestBySupplies(Long requestId, ApprovalDto approvalDto);

    List<CommentDto> getCommentsByRequestId(Long requestId);

    List<ApprovalDto> getApprovalsByRequestId(Long requestId);



    //get sub div list to create a request
    List<SubdivDto> getAllSubdivs();

    List<SubdivDto> getSubdivsByAdmindivId(Long id);

    //get sub div list grouped by its admindiv
    List<SubdivGroupedDto> getGroupedSubdivs();

    List<AdmindivDto> getAllAdmindivs();

    List<SubdivDto> getSubdivs();

}
