package com.cht.procurementManagement.services.supplies;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.enums.ApprovalType;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.ReviewType;
import com.cht.procurementManagement.enums.UserRole;
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
import java.util.List;
import java.util.Map;
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

    //get all requests
    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequests() {
        return requestRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Request::getId).reversed())
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }



    //get requests pending approval by supplies division
    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getRequestsPendingSuppliesApproval() {
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING_SUPPLIES_APPROVAL)
                .sorted(Comparator.comparing(Request::getId).reversed())
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getRequestsApproved() {
        return requestRepository.findAll()
                .stream()
                .filter(request -> request.getStatus() == RequestStatus.PENDING_PROCUREMENT)
                .sorted(Comparator.comparing(Request::getId).reversed())
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto getRequestById(Long requestId) {
        //using request service method
        return requestService.getRequestById(requestId);
    }

    //create request
    @Override
    public RequestDto createRequestBySupplies(RequestDto requestDto) {

        //1. check for sub divs list
        if(requestDto.getSubdivIdList() != null && !requestDto.getSubdivIdList().isEmpty()) {

            //check if sub divs exist in db
            if(!checkIfSubdivIdsExist(requestDto.getSubdivIdList())) {
                throw new RuntimeException("Some sub divisions entered may not exist!");
            }


            //2. set status of request
            requestDto.setStatus(RequestStatus.PENDING_PROCUREMENT);

            //3. create request through request service
            return requestService.createRequest(requestDto);

        }else{
            throw new EntityNotFoundException("Sub-division/s not found!");
        }
    }


    //reject a request from a sub div - create comment & change request status
    @Override
    public CommentDto rejectRequestBySupplies(Long requestId, CommentDto commentDto) {

        //1. find the request object to reject
        Request existingRequest = requestRepository.findById(requestId)
                .orElseThrow( () ->  new RuntimeException("Request not found"));

        //2. check status
        if(!existingRequest.getStatus().equals(RequestStatus.PENDING_SUPPLIES_APPROVAL)) {
            throw new RuntimeException("Request is not due for review");
        }

        //3. change status of request
        existingRequest.setStatus(RequestStatus.REJECTED_SUPPLIES_APPROVAL);

        //4. save request to db
        requestRepository.save(existingRequest);

        //5. update commentDto with request id and type
        commentDto.setType(ReviewType.SUPPLIES);
        commentDto.setRequestId(requestId);

        //6. create comment through comment service
        return commentService.createComment(commentDto);

    }

    @Override
    public ApprovalDto approveRequestBySupplies(Long requestId, ApprovalDto approvalDto) {

        //1. find the request object to approve
        Request existingRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request is not found"));

        //2. check for status
        if(!existingRequest.getStatus().equals(RequestStatus.PENDING_SUPPLIES_APPROVAL)) {
            throw new RuntimeException("Request is not due for review");
        }


        //3. change request status
        existingRequest.setStatus(RequestStatus.PENDING_PROCUREMENT);

        //4. save request to db
        requestRepository.save(existingRequest);

        //5. update Approval dto with request id and type
        approvalDto.setType(ApprovalType.SUPPLIES);
        approvalDto.setRequestId(requestId);

        //6.create new approval object through Approval service
        return approvalService.createApproval(approvalDto);

        }

    @Override
    public List<CommentDto> getCommentsByRequestId(Long requestId) {
        //1. find the request object to reject
        Request existingRequest = requestRepository.findById(requestId)
                .orElseThrow( () ->  new RuntimeException("Request not found"));
        return commentService.getCommentsByRequestId(requestId);
    }

    @Override
    public List<ApprovalDto> getApprovalsByRequestId(Long requestId) {
        //1. find the request object to reject
        Request existingRequest = requestRepository.findById(requestId)
                .orElseThrow( () ->  new RuntimeException("Request not found"));
        return approvalService.getApprovalsByRequestId(requestId);
    }

    @Override
    public RequestDto updateRequest(Long requestId, RequestDto requestDto) {
        //check for validation for supplies user - using class method
        Request existingRequest = validateRequestForUpdateDeleteForSuppliesUser(requestId);

        //2. check for sub div list in new request dto
        if(requestDto.getSubdivIdList() != null && !requestDto.getSubdivIdList().isEmpty()) {


            //check if sub divs exist in db
            if(!checkIfSubdivIdsExist(requestDto.getSubdivIdList())) {
                throw new RuntimeException("Some sub divisions entered may not exist!");
            }

            //3. set status of request
            requestDto.setStatus(RequestStatus.PENDING_PROCUREMENT);

            //4. create request through request service
            return requestService.updateRequest(existingRequest, requestDto);

        }else{
            throw new EntityNotFoundException("Sub-division/s in updated request not found!");
        }

    }

    @Override
    public void deleteRequest(Long requestId) {
        //check for validation for supplies user - using class method
        Request existingRequest = validateRequestForUpdateDeleteForSuppliesUser(requestId);
        //delete request
        requestRepository.deleteById(requestId);
    }

    @Override
    public List<SubdivDto> getAllSubdivs() {
        return subdivRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Subdiv::getName))
                .map(Subdiv::getSubdivDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubdivGroupedDto> getGroupedSubdivs() {
        //get all sub divs from db
        List<Subdiv> subdivList = subdivRepository.findAll();

        Map<Long, List<Subdiv>> groupedSubdiv = subdivList
                .stream()
                .collect(Collectors.groupingBy(subdiv -> subdiv.getAdmindiv().getId()));

        return groupedSubdiv.entrySet()
                .stream()
                .map(entry -> {
                    Long admindivId = entry.getKey();

                    List<Subdiv> subdivs = entry.getValue();
                    String admindivName = subdivs.get(0).getAdmindiv().getName();

                    List<SubdivDto> subdivDtos = subdivs
                            .stream()
                            .sorted(Comparator.comparing(Subdiv::getName))
                            .map(Subdiv::getSubdivDto)
                            .collect(Collectors.toList());

                    return new SubdivGroupedDto(admindivId, admindivName, subdivDtos);
                })
                .sorted(Comparator.comparing(SubdivGroupedDto::getAdmindivName))
                .collect(Collectors.toList());
    }


    private Request validateRequestForUpdateDeleteForSuppliesUser(Long requestId){

            //1. find the request object to reject
            Request existingRequest = requestRepository.findById(requestId)
                    .orElseThrow( () ->  new RuntimeException("Request not found"));

            //3. check created user
            if(!UserRole.SUPPLIESUSER.equals(existingRequest.getCreatedBy().getUserRole())){
                throw new RuntimeException("Request is not created by the supplies division");
            }

            //2. check status
            if(!existingRequest.getStatus().equals(RequestStatus.PENDING_PROCUREMENT)) {
                throw new RuntimeException("Not allowed to update/delete due to current status of request");
            }

            return existingRequest;
        }


    private boolean checkIfSubdivIdsExist(List<Long> subdivIds){
        //count in the request
        Long uniqueInputCount = subdivIds.stream().distinct().count();

        //count in db
        Long dbCount = subdivRepository.countByIdIn(subdivIds);

        //compare to find if it equals
        return uniqueInputCount == dbCount;
    }
}
