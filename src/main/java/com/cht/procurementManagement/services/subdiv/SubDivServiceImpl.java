package com.cht.procurementManagement.services.subdiv;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.enums.RequestStatus;
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
        //update the requestDto with subdiv List
        requestDto.setSubdivIdList(subdivList);
        //set the status of requestDto
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
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }


    //get request by id
    @Override
    public RequestDto getRequestById(Long id) {
        return requestRepository.findById(id)
                .map(Request::getRequestDto)
                .orElseThrow(() -> new EntityNotFoundException("Request is not found"));
    }

    @Override
    public SubdivDto getSubdiv() {
        return subdivRepository.findById(getSubdivIdofLoggedUser())
                .map(Subdiv::getSubdivDto)
                .orElseThrow( () -> new EntityNotFoundException("Subdiv is not found"));
    }

    @Override
    public List<CommentDto> getCommentsByRequestId(Long id) {
        //getting user's sub-div details from logged details - using class method
        Long subdivIdOfUser = getSubdivIdofLoggedUser();

        //find the Request object from db
        Request request = requestRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Request not found"));

        //the sub div list of the request
        List<Subdiv> requestSubdivList = request.getSubdivList();

        //check size and id of the subdiv
        if(requestSubdivList.size() ==1 && requestSubdivList.get(0).getId().equals(subdivIdOfUser)){
            return commentService.getCommentsByRequestId(id);
        }else{
            throw new RuntimeException("Request has more sub-divisions.");
        }


    }

    @Override
    public List<ApprovalDto> getApprovalsByRequestId(Long id){

        //getting user's sub-div details from logged details - using class method
        Long subdivIdOfUser = getSubdivIdofLoggedUser();

        //find the Request object from db
        Request request = requestRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Request not found"));

        //the sub div list of the request
        List<Subdiv> requestSubdivList = request.getSubdivList();

        //check size and id of the subdiv
        if(requestSubdivList.size() ==1 && requestSubdivList.get(0).getId().equals(subdivIdOfUser)){
            return approvalService.getApprovalsByRequestId(id);
        }else{
            throw new RuntimeException("Request has more sub-divisions.");
        }
    }

    @Override
    public void deleteRequestById(Long id) {
        //getting user's sub-div details from logged details - using class method
        Long subdivIdOfUser = getSubdivIdofLoggedUser();

        //find the Request object from db
        Request request = requestRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Request not found"));

        //check for correct status
        if(request.getStatus() != RequestStatus.PENDING_ADMIN_APPROVAL){
            throw new RuntimeException("Status is not valid for deletion");
        }

        //the sub div list of the request
        List<Subdiv> requestSubdivList = request.getSubdivList();
        //check size and id of the subdiv
        if(requestSubdivList.size() ==1 && requestSubdivList.get(0).getId().equals(subdivIdOfUser)){
            requestService.deleteRequest(id);
        }else{
            throw new RuntimeException("Request has more sub-divisions.");
        }
    }

    @Override
    public RequestDto updateRequestById(Long id, RequestDto requestDto) {

        //find the Request object from db
        Request request = requestRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Request not found"));

        //check for correct status
        if(request.getStatus() != RequestStatus.PENDING_ADMIN_APPROVAL){
            throw new RuntimeException("Status is not valid for deletion");
        }

        //getting user's sub-div details from logged details - using class method
        Long subdivIdOfUser = getSubdivIdofLoggedUser();
        //creating a list for sub div ids with the sub div id - need to pass as a List
        List<Long> subdivList = List.of(subdivIdOfUser);
        //update the requestDto with subdiv List
        requestDto.setSubdivIdList(subdivList);

        //the sub div list of the request
        List<Subdiv> requestSubdivList = request.getSubdivList();
        //check size and id of the subdiv
        if(requestSubdivList.size() ==1 && requestSubdivList.get(0).getId().equals(subdivIdOfUser)){
            //set the status of requestDto
            requestDto.setStatus(RequestStatus.PENDING_ADMIN_APPROVAL);
            return requestService.updateRequest(id, requestDto);
        }else{
            throw new RuntimeException("Request has more sub-divisions.");
        }
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



}
