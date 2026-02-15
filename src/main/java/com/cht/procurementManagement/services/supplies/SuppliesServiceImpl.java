package com.cht.procurementManagement.services.supplies;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.enums.ApprovalType;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.ReviewType;
import com.cht.procurementManagement.repositories.RequestRepository;
import com.cht.procurementManagement.repositories.SubdivRepository;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.Approval.ApprovalService;
import com.cht.procurementManagement.services.Comment.CommentService;
import com.cht.procurementManagement.services.auth.AuthService;
import com.cht.procurementManagement.services.requests.RequestService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SuppliesServiceImpl implements SuppliesService {
    private final RequestRepository requestRepository;

    //to get logged user details
    private final AuthService authService;
    private final UserRepository userRepository;
    private final SubdivRepository subdivRepository;
    private final CommentService commentService;
    private final RequestService requestService;
    private final ApprovalService approvalService;

    public SuppliesServiceImpl(RequestRepository requestRepository,
                               AuthService authService,
                               UserRepository userRepository,
                               SubdivRepository subdivRepository,
                               CommentService commentService,
                               RequestService requestService,
                               ApprovalService approvalService) {
        this.requestRepository = requestRepository;
        this.authService = authService;
        this.userRepository = userRepository;
        this.subdivRepository = subdivRepository;
        this.commentService = commentService;
        this.requestService = requestService;
        this.approvalService = approvalService;
    }

    //get requests pending approval by supplies division
    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getRequestsPendingSuppliesApproval() {
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING_SUPPLIES_APPROVAL)
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }

    //create request
    @Override
    public RequestDto createRequestBySupplies(RequestDto requestDto) {

        if(requestDto.getSubdivIdList() != null && !requestDto.getSubdivIdList().isEmpty()) {
            //set status of request
            requestDto.setStatus(RequestStatus.PENDING_PROCUREMENT);
            //create request through request service
            return requestService.createRequest(requestDto);
        }else{
            throw new EntityNotFoundException("Sub-division/s not found!");
        }
    }

    //get all requests
    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequests() {
        return requestRepository.findAll()
                .stream()
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }


    //reject a request from a sub div - create comment & change request status
    @Override
    public CommentDto rejectRequestBySupplies(Long requestId, CommentDto commentDto) {
        //find the request object to reject
        Optional<Request> optionalRequest= requestRepository.findById(requestId);
        //change the status & create a comment
        if(optionalRequest.isPresent()){

            Request existingRequest = optionalRequest.get();
            //check status
            if(existingRequest.getStatus().equals(RequestStatus.PENDING_SUPPLIES_APPROVAL)) {
                //change status
                existingRequest.setStatus(RequestStatus.REJECTED_SUPPLIES_APPROVAL);
                //save request to db
                requestRepository.save(existingRequest);

                //update commentDto with request id and type
                commentDto.setType(ReviewType.SUPPLIES);
                commentDto.setRequestId(requestId);
                //create comment through comment service
                return commentService.createComment(commentDto);
            }else{
                throw new RuntimeException("Request is not due for review");
            }
        }else{
            throw new EntityNotFoundException("Request is not found");
        }
    }

    @Override
    public ApprovalDto approveRequestBySupplies(Long requestId, ApprovalDto approvalDto) {
       //find the request object to approve
        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        //change the status of request & create new approval object
        if(optionalRequest.isPresent()){

            Request existingRequest = optionalRequest.get();
            //check for status
            if(existingRequest.getStatus().equals(RequestStatus.PENDING_SUPPLIES_APPROVAL)) {
                //change request status
                existingRequest.setStatus(RequestStatus.PENDING_PROCUREMENT);
                //save request to db
                requestRepository.save(existingRequest);

                //update Approval dto with request id and type
                approvalDto.setType(ApprovalType.SUPPLIES);
                approvalDto.setRequestId(requestId);
                //create new approval object through Approval service
                return approvalService.createApproval(approvalDto);
            }else{
                throw new RuntimeException("Request is not due for review");
            }
        }else{
            throw new EntityNotFoundException("Request not found");
        }
    }

}
