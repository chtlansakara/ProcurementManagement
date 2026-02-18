package com.cht.procurementManagement.services.subdiv;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.AdmindivRepository;
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


import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class SubDivServiceImpl implements SubDivService {


    private final UserRepository userRepository;
    private final SubdivRepository subdivRepository;
    private final RequestRepository requestRepository;
    //to get logged user details
    private final AuthService authService;
    private final RequestService requestService;
    private final CommentService commentService;
    private final ApprovalService approvalService;
    private final AdmindivRepository admindivRepository;
    public SubDivServiceImpl(UserRepository userRepository,
                             SubdivRepository subdivRepository,
                             RequestRepository requestRepository,
                             AuthService authService,
                             RequestService requestService,
                             CommentService commentService,
                             ApprovalService approvalService,
                             AdmindivRepository admindivRepository) {
        this.userRepository = userRepository;
        this.subdivRepository = subdivRepository;
        this.requestRepository = requestRepository;
        this.authService = authService;
        this.requestService = requestService;
        this.commentService = commentService;
        this.approvalService = approvalService;
        this.admindivRepository = admindivRepository;
    }

    @Transactional
    @Override
    public RequestDto createRequestBySubdiv(RequestDto requestDto) {
        //sub div of the user & the status is set here

        //getting user's sub-div details from logged details - using class method
        Long subdivIdOfUser = getSubdivIdofLoggedUser();
        //creating a list for sub div ids with the sub div id - need to pass as a List
        List<Long> subdivList = List.of(subdivIdOfUser);
        //1. update the requestDto with subdiv List
        requestDto.setSubdivIdList(subdivList);
        //2. set the status of requestDto
        requestDto.setStatus(RequestStatus.PENDING_ADMIN_APPROVAL);
        //create request through request service - sets the user createdBy and created Date
        return requestService.createRequest(requestDto);
    }



    //get all requests ONLY for sub-div
    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequestsOnlyBySubdivId() {
        //getting sub div id of the user logged in
        return  requestRepository.findAllRequestsOnlyBySubdivId(getSubdivIdofLoggedUser())
                .stream()
                .sorted(Comparator.comparing(Request::getId).reversed())
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }


    //get request by id
    @Override
    public RequestDto getRequestById(Long id) {
        //validate as sub-div request & return as dto
       return validateAsSubdivRequest(id).getRequestDto();
    }

    //get sub-div dto of the logged user
    @Override
    public SubdivDto getSubdiv() {
        return subdivRepository.findById(getSubdivIdofLoggedUser())
                .map(Subdiv::getSubdivDto)
                .orElseThrow( () -> new EntityNotFoundException("Subdiv is not found"));
    }

    @Override
    public List<CommentDto> getCommentsByRequestId(Long id) {

        Request request = validateAsSubdivRequest(id);
        return commentService.getCommentsByRequestId(id);

    }

    @Override
    public List<ApprovalDto> getApprovalsByRequestId(Long id){

        Request request = validateAsSubdivRequest(id);
        return approvalService.getApprovalsByRequestId(id);
    }

    @Override
    public void deleteRequestById(Long id) {
        //1. Check authorization for the request for sub-div user - using class method
        Request existingRequest = validateRequestUpdateDeleteForSubdivUser(id);
        //2. Delete using request servie
        requestService.deleteRequest(existingRequest);
    }

    @Transactional
    @Override
    public RequestDto updateRequestById(Long id, RequestDto requestDto) {

        //1. Check authorization for the request for sub-div user - using class method
        Request existingRequest = validateRequestUpdateDeleteForSubdivUser(id);

        //2. set sub div list of the request dto -- as user's
        //getting user's sub-div details from logged details - using class method
        Long subdivIdOfUser = getSubdivIdofLoggedUser();
        requestDto.setSubdivIdList(List.of(subdivIdOfUser));

        //3. set status of dto:
        requestDto.setStatus(RequestStatus.PENDING_ADMIN_APPROVAL);

        return requestService.updateRequest(existingRequest, requestDto);
    }


    //get all requests RELATED to sub-div
    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequestsRelatedBySubdivId() {
        //getting sub div id of the user logged in
        return  requestRepository.findAllRequestsRelatedBySubdivId(getSubdivIdofLoggedUser())
                .stream()
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }


    //class method to get sub div id from the logged user details
    private Long getSubdivIdofLoggedUser(){
        //getting sub-div id from the logged details
        UserDto loggedUser = authService.getLoggedUserDto();
        Long subdivIdOfUser = loggedUser.getSubdivId();
        return  subdivIdOfUser;
    }

    //authorization - validate method for update and delete a request by a sub-div user
    private Request validateRequestUpdateDeleteForSubdivUser(Long requestId){
        //find the request of object
        Request request = requestRepository.findById(requestId)
                .orElseThrow( () -> new RuntimeException("Request not found"));

        //get sub-div list of request
        List<Subdiv> requestSubdivList =  request.getSubdivList();

        //get logged user id
        Long subdivIdOfUser = getSubdivIdofLoggedUser();

        //1. check for single sub-div in the list
        if(requestSubdivList == null || requestSubdivList.size() != 1){
            throw new RuntimeException("Request has more than one sub-division");
        }

        //2. check if it's the user's sub-division
        if(!requestSubdivList.get(0).getId().equals(subdivIdOfUser)){
            throw new RuntimeException("Not authorized to access other sub-division's requests");
        }

        //3. check for status
        if(!RequestStatus.PENDING_ADMIN_APPROVAL.equals(request.getStatus())){
            throw new RuntimeException("Not allowed to update/delete due to current status of request");
        }

        //4. check user role
        if(!UserRole.SUBDIVUSER.equals(request.getCreatedBy().getUserRole())){
            throw new RuntimeException("Request is not created by the sub-division");
        }

        return request;

    }

    //check sub-div requests
    private Request validateAsSubdivRequest(Long requestId){
        //find the request object
        Request request = requestRepository.findById(requestId)
                .orElseThrow( () -> new RuntimeException("Request not found"));

        //get sub-div list of request
        List<Subdiv> requestSubdivList =  request.getSubdivList();

        //get logged user id
        Long subdivIdOfUser = getSubdivIdofLoggedUser();

        //1. check for single sub-div in the list
        if(requestSubdivList == null || requestSubdivList.size() != 1){
            throw new RuntimeException("Request has more than one sub-division");
        }

        //2. check if it's the user's sub-division
        if(!requestSubdivList.get(0).getId().equals(subdivIdOfUser)){
            throw new RuntimeException("Not authorized to access other sub-division's requests");
        }

        return request;

    }

}
