package com.cht.procurementManagement.services.admindiv;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.entities.Approval;
import com.cht.procurementManagement.entities.Comment;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.enums.ApprovalType;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.ReviewType;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.*;
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

    private final CommentRepository commentRepository;
    private final ApprovalRepository approvalRepository;

    public AdminDivServiceImpl(RequestRepository requestRepository,
                               SubdivRepository subdivRepository,
                               UserRepository userRepository,
                               AuthService authService,
                               RequestService requestService,
                               CommentService commentService,
                               ApprovalService approvalService,
                               CommentRepository commentRepository,
                               ApprovalRepository approvalRepository) {
        this.requestRepository = requestRepository;
        this.subdivRepository = subdivRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.requestService = requestService;
        this.commentService = commentService;
        this.approvalService = approvalService;
        this.commentRepository = commentRepository;
        this.approvalRepository = approvalRepository;
    }


    //get only requests belong to admin div
   @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequestsOnlyByAdmindivId() {
        //1. finding id list of the admin division's sub divs
        List<Long> subdivIdList = subdivRepository.findByAdmindivId(getAdmindivIdofLoggedUser())
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());
        //2. find requests with that sub-div ids & return as dto list (sorted by request id)
        return requestRepository.findAllRequestsOnlyBySubdivIdList(subdivIdList)
                .stream()
                .sorted(Comparator.comparing(Request::getId).reversed())
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }


    //get only - pending requests
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
                .sorted(Comparator.comparing(Request::getId).reversed())
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }

    //get only - approved requests
    @Override
    public List<RequestDto> getRequestsApprovedByAdmindiv() {
        //finding the admin div's subdiv list
        List<Long> subdivIdList = subdivRepository.findByAdmindivId(getAdmindivIdofLoggedUser())
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());
        //return filtered requests by status
        return requestRepository.findAllRequestsOnlyBySubdivIdList(subdivIdList)
                .stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING_SUPPLIES_APPROVAL && request.getCreatedBy().getUserRole() == UserRole.SUBDIVUSER)
                .sorted(Comparator.comparing(Request::getId).reversed())
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }

    //get only - rejected requests
    @Override
    public List<RequestDto> getRequestsRejectedByAdmindiv() {
        //finding the admin div's subdiv list
        List<Long> subdivIdList = subdivRepository.findByAdmindivId(getAdmindivIdofLoggedUser())
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());
        //return filtered requests by status
        return requestRepository.findAllRequestsOnlyBySubdivIdList(subdivIdList)
                .stream()
                .filter(request -> request.getStatus() == RequestStatus.REJECTED_ADMIN_APPROVAL)
                .sorted(Comparator.comparing(Request::getId).reversed())
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

    //get request by id
    @Override
    public RequestDto getRequestByRequestId(Long requestId) {
        //find if the request's sub div list only has admin div's sub divs
        RequestDto retrieveRequestDto =  requestService.getRequestById(requestId);

        //check if sub div id list has other admin division's
        if(!checkforCorrectSubdivsForAdmindiv(retrieveRequestDto.getSubdivIdList())){
            throw new RuntimeException("Include sub-divisions don't belong to the admin division.");
        }

        return retrieveRequestDto;
    }


    @Transactional
    @Override
    public RequestDto createRequestByAdmindiv(RequestDto requestDto) {

        //1. checking if the subdiv list is empty
        if(requestDto.getSubdivIdList() != null && !requestDto.getSubdivIdList().isEmpty()){

            //2. check sub div list given

            //i. check if sub div id list has other sub divs belong to other admin division's - use class method
            if(!checkforCorrectSubdivsForAdmindiv(requestDto.getSubdivIdList())){
                throw new RuntimeException("Include sub-divisions don't belong to the admin division.");
            }

            //3. set status of request
            requestDto.setStatus(RequestStatus.PENDING_SUPPLIES_APPROVAL);

            //4. create request through request service - set status & sub div list here
            return requestService.createRequest(requestDto);

        }else{
            throw new EntityNotFoundException("Sub-division/s are empty!");
        }
    }

    @Override
    public RequestDto updateRequestByRequestId(Long requestId, RequestDto requestDto) {
        //1. check for authorization of current request:
        Request existingRequest  = validateRequestForUpdateDeleteForAdmindivUser(requestId);

        //2. now to check new sub div list sent -
        // i. get  new request dto's subdiv list
        List<Long> requestSubdivIds = requestDto.getSubdivIdList();
        if(requestSubdivIds ==null || requestSubdivIds.isEmpty()){
            throw new RuntimeException("Sub divisions can not be empty");
        }
        //ii. compare with allowed id list
         if(!checkforCorrectSubdivsForAdmindiv(requestSubdivIds)){
             throw new RuntimeException("Request updated contains other unauthorized sub-divisions.");
         }

        //3. set the status of the request
        requestDto.setStatus(RequestStatus.PENDING_SUPPLIES_APPROVAL);

         //4. update using Request Service
        return requestService.updateRequest(existingRequest, requestDto);
    }

    @Override
    public void deleteRequest(Long requestId) {

        //1. check for authorization
        Request existingRequest = validateRequestForUpdateDeleteForAdmindivUser(requestId);
        //2. then delete using Request service
        requestService.deleteRequest(existingRequest);

    }


    //get reject comments for a request  by id
    @Override
    public List<CommentDto> getCommentsByRequestId(Long requestId) {
        Request request = validateAsAdmindivRequest(requestId);
        return commentService.getCommentsByRequestId(requestId);
    }

    //get approvals by request id
    @Override
    public List<ApprovalDto> getApprovalsByRequestId(Long requestId) {
      Request request = validateAsAdmindivRequest(requestId);
      return approvalService.getApprovalsByRequestId(requestId);
    }

    //reject request - create a comment & change request status
    @Override
    public CommentDto rejectRequestByAdmindiv(Long requestId, CommentDto commentDto) {
        //1. check if request has only sub-divs of admin div
        Request existingRequest = validateAsAdmindivRequest(requestId);

        //2. check for correct status
        if(existingRequest.getStatus().equals(RequestStatus.PENDING_ADMIN_APPROVAL)) {

            //3. Create reject comment:
            //i. check if a reject(comment) or approval for the request id already present from admin div - get if any from db
            List<CommentDto> commentsForRequest = commentRepository.findAllByRequestIdAndType(requestId, ReviewType.ADMIN_DIV)
                    .stream().map(Comment::getCommentDto).collect(Collectors.toList());
            List<ApprovalDto> approvalsForRequst = approvalRepository.findAllByRequestIdAndType(requestId, ApprovalType.ADMIN_DIV)
                    .stream().map(Approval::getApprovalDto).collect(Collectors.toList());
            //check if there is any - can not create reject comment
            if(!commentsForRequest.isEmpty() && !approvalsForRequst.isEmpty() ){
                throw new RuntimeException("Already reviewed request!");
            }

            //4. change status to rejected
            existingRequest.setStatus(RequestStatus.REJECTED_ADMIN_APPROVAL);
            //5. save request to db
            requestRepository.save(existingRequest);

            //6. update obtained commentDto with type & relevant request id
            commentDto.setType(ReviewType.ADMIN_DIV);
            commentDto.setRequestId(requestId);

            //7. create comment through comment service
            return commentService.createComment(commentDto);
        }else{
            throw new RuntimeException("Request is not due for review");
        }



    }


    //create approval - create approval & change request status
    @Override
    public ApprovalDto approveRequestByAdmindiv(Long requestId, ApprovalDto approvalDto) {
        //1. check if request has only sub-divs of admin div
        Request existingRequest = validateAsAdmindivRequest(requestId);

        //2. check for correct status
        if(existingRequest.getStatus().equals(RequestStatus.PENDING_ADMIN_APPROVAL)) {


                //3. To create approval:
                //i. check if a reject or approval for the request id already present from admin div - get from db
                List<CommentDto> commentsForRequest = commentRepository.findAllByRequestIdAndType(requestId, ReviewType.ADMIN_DIV)
                        .stream().map(Comment::getCommentDto).collect(Collectors.toList());
                List<ApprovalDto> approvalsForRequst = approvalRepository.findAllByRequestIdAndType(requestId, ApprovalType.ADMIN_DIV)
                        .stream().map(Approval::getApprovalDto).collect(Collectors.toList());
                //ii. check if there is any
                if(!commentsForRequest.isEmpty() && !approvalsForRequst.isEmpty() ){
                    throw new RuntimeException("Already reviewed request!");
                }

                //4. change request status to approved
                existingRequest.setStatus(RequestStatus.PENDING_SUPPLIES_APPROVAL);

                //5. save request to db
                requestRepository.save(existingRequest);

                //6. update ApprovalDto with request id and type
                approvalDto.setType(ApprovalType.ADMIN_DIV);
                approvalDto.setRequestId(requestId);

                //7.create new approval object through Approval service
                return approvalService.createApproval(approvalDto);
            }else{
                throw new RuntimeException("Request is not due for review");
            }

    }

    @Override
    public List<SubdivDto> getSubdivList() {
        //get admindiv of the logger user
        Long admindivId = getAdmindivIdofLoggedUser();

        //get & return subdiv list of the admindiv
        return subdivRepository.findByAdmindivId(admindivId)
                .stream()
                .sorted(Comparator.comparing(Subdiv::getName))
                .map(Subdiv::getSubdivDto)
                .collect(Collectors.toList());

    }

    //method to get admindiv of the logged user
    private Long getAdmindivIdofLoggedUser(){
        //getting user details from logged details
        UserDto loggedUser = authService.getLoggedUserDto();
        //finding his admin div
        Long admindivIdOfUser = loggedUser.getAdmindivId();
        return admindivIdOfUser;
    }

    //used with create request
    private boolean checkforCorrectSubdivsForAdmindiv(List<Long> requestSubdivIdList) {

        Long admindivId = getAdmindivIdofLoggedUser();

        //get subdiv list of the admindiv
        List<Subdiv> subdivListofAdmindiv = subdivRepository.findByAdmindivId(admindivId);
        //converting to ids Set
        Set<Long> allowedSubdivIds = subdivListofAdmindiv
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toSet());

        //compare and return if it contains match
        return requestSubdivIdList
                .stream()
                .allMatch(allowedSubdivIds::contains);
    }

    //check if the request only has the sub-division that belongs to the logged user's admin div
    private boolean checkIfRequestIsValidforAdmindivByRequestId(Long requestId){

        //find the request object - from db
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        //find the logged user's admin div id - using class's method
        Long admindivId = getAdmindivIdofLoggedUser();

        //instead of comparing objects, it's better to compare ids of sub divs as lists

        //find ids of sub-divs list of the admin div from db
        List<Long> admindivSubdivIds = subdivRepository.findByAdmindivId(admindivId)
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());

        //get request's
        List<Long> requestSubdivIds = request.getSubdivList()
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());

        //now compare two lists and return if request's only contains ids of admin div's
        return admindivSubdivIds.containsAll(requestSubdivIds);

    }

    private Request validateAsAdmindivRequest(Long requestId){

        //1. get requests sub-div id list
        //find the request object
        Request request = requestRepository.findById(requestId)
                .orElseThrow( () -> new RuntimeException("Request not found"));

        //get sub-div id list of the request - by mapping
        List<Long> requestSubdivIds = request.getSubdivList()
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());


        //2. get admin divs sub-div id list
        //find the logged user's admin div id - using class's method
        Long admindivId = getAdmindivIdofLoggedUser();

        //instead of comparing objects, it's better to compare ids of sub divs as lists
        //find ids of sub-divs list of the admin div from db
        List<Long> admindivSubdivIds = subdivRepository.findByAdmindivId(admindivId)
                .stream()
                .map(Subdiv::getId)
                .collect(Collectors.toList());

        //validate if the request contains other sub-divs
        if(!admindivSubdivIds.containsAll(requestSubdivIds)){
            throw new RuntimeException("Request contains sub-divisions that doesn't belong to the admin division");
        }

        return request;

    }

    private Request validateRequestForUpdateDeleteForAdmindivUser(Long requestId){
        //1. check if request belongs to admin div
        Request request = validateAsAdmindivRequest(requestId);

        if(request != null){
            //2. check for status
            if(!RequestStatus.PENDING_SUPPLIES_APPROVAL.equals(request.getStatus())){
                throw new RuntimeException("Not allowed to update/delete due to current status of request");
            }

            //3. check user role
            if(!UserRole.ADMINDIVUSER.equals(request.getCreatedBy().getUserRole())){
                throw new RuntimeException("Request is not created by the admin-division");
            }
        }else{
            throw new RuntimeException("Some error related to request and admindiv");
        }
        return request;
    }
}
