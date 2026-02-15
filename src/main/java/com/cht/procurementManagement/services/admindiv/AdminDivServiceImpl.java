package com.cht.procurementManagement.services.admindiv;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.Subdiv;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminDivServiceImpl implements AdminDivService {

    private final RequestRepository requestRepository;

    private final SubdivRepository subdivRepository;

    private final UserRepository userRepository;
    //to get logged user details
    private final AuthService authService;
    private final RequestService requestService;

    private final CommentService commentService;
    private final ApprovalService approvalService;

    public AdminDivServiceImpl(RequestRepository requestRepository,
                               SubdivRepository subdivRepository,
                               UserRepository userRepository,
                               AuthService authService,
                               RequestService requestService,
                               CommentService commentService,
                               ApprovalService approvalService) {
        this.requestRepository = requestRepository;
        this.subdivRepository = subdivRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.requestService = requestService;
        this.commentService = commentService;
        this.approvalService = approvalService;
    }


    //get only requests belong to admin div
   @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequestsOnlyByAdmindivId() {
        //finding the admin div's subdiv list
        List<Long> subdivIdList = subdivRepository.findByAdmindivId(getAdmindivIdofLoggedUser())
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());
        return requestRepository.findAllRequestsOnlyBySubdivIdList(subdivIdList)
                .stream()
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getRequestsPendingAdmindivApproval(){
        //finding the admin div's subdiv list
        List<Long> subdivIdList = subdivRepository.findByAdmindivId(getAdmindivIdofLoggedUser())
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());
        //return filtered requests by status
        return requestRepository.findAllRequestsOnlyBySubdivIdList(subdivIdList)
                .stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING_ADMIN_APPROVAL)
                .sorted(Comparator.comparing(Request::getApprovedDate))
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }

    //get RELATED requests by admin div id
    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequestsRelatedByAdmindivId() {
        //finding the admin div's subdiv list
        List<Long> subdivIdList = subdivRepository.findByAdmindivId(getAdmindivIdofLoggedUser())
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());
        return requestRepository.findAllRequestsRelatedBySubdivIdList(subdivIdList)
                .stream()
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }
    @Transactional
    @Override
    public RequestDto createRequestByAdmindiv(RequestDto requestDto) {

        if(requestDto.getSubdivIdList() != null && !requestDto.getSubdivIdList().isEmpty()){

            //get admin-div of the logged user using a class method
            Long admindivId = getAdmindivIdofLoggedUser();
            //check if sub div id list has other admin division's
            if(!checkforCorrectSubdivs(requestDto.getSubdivIdList(), admindivId)){
                throw new RuntimeException("Include sub-divisions don't belong to the admin division.");
            }

            //set status of request
            requestDto.setStatus(RequestStatus.PENDING_SUPPLIES_APPROVAL);
            //create request through request service
            return requestService.createRequest(requestDto);

        }else{
            throw new EntityNotFoundException("Sub-division is not found!");
        }
    }

    //reject request - create a comment & change request status
    @Override
    public CommentDto rejectRequestByAdmindiv(Long requestId, CommentDto commentDto) {
        //change request status to rejected
        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        if(optionalRequest.isPresent()){


           Request existingRequest = optionalRequest.get();
           //check for status
           if(existingRequest.getStatus().equals(RequestStatus.PENDING_ADMIN_APPROVAL)) {
               //change status
               existingRequest.setStatus(RequestStatus.REJECTED_ADMIN_APPROVAL);
               //save request to db
               requestRepository.save(existingRequest);

               //update commentDto with type & request id
               commentDto.setType(ReviewType.ADMIN_DIV);
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
    public ApprovalDto approveRequestByAdmindiv(Long requestId, ApprovalDto approvalDto) {
        //find the request object to approve
        Optional<Request> optionalRequest = requestRepository.findById(requestId);
        //change the status of request & create new approval object
        if(optionalRequest.isPresent()){
            //change request status
            Request existingRequest = optionalRequest.get();
            //check for request status
            if(existingRequest.getStatus().equals(RequestStatus.PENDING_ADMIN_APPROVAL)) {
                existingRequest.setStatus(RequestStatus.PENDING_SUPPLIES_APPROVAL);
                //save request to db
                requestRepository.save(existingRequest);

                //update Approval dto with request id and type
                approvalDto.setType(ApprovalType.ADMIN_DIV);
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

    //method to get admindiv of the logged user
    private Long getAdmindivIdofLoggedUser(){
        //getting user details from logged details
        UserDto loggedUser = authService.getLoggedUserDto();
        //finding his admin div
        Long admindivIdOfUser = loggedUser.getAdmindivId();
        return admindivIdOfUser;
    }

    private boolean checkforCorrectSubdivs(List<Long> checkSubdivIdList, Long admindivId) {

        //get subdiv list of the admindiv
        List<Subdiv> subdivList = subdivRepository.findByAdmindivId(admindivId);
        //converting to ids Set
        Set<Long> allowedIds = subdivList
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toSet());
        //compare and return if it contains match
        return checkSubdivIdList
                .stream()
                .allMatch(allowedIds::contains);
    }
}
