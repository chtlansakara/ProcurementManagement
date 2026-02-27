package com.cht.procurementManagement.services.procurement;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.dto.procurement.ProcurementCreateDto;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.dto.procurement.ProcurementStatusUpdateDto;
import com.cht.procurementManagement.entities.*;
import com.cht.procurementManagement.enums.*;
import com.cht.procurementManagement.mappers.ProcurementMapper;
import com.cht.procurementManagement.repositories.*;
import com.cht.procurementManagement.services.auth.AuthService;
import com.cht.procurementManagement.utils.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class ProcurementServiceImpl implements ProcurementService{
    private final ProcurementMapper procurementMapper;
    private final UserRepository userRepository;
    private final ProcurementStatusRepository procurementStatusRepository;
    private final VendorRepository vendorRepository;
    private final RequestRepository requestRepository;
    private final AuthService authService;
    private final ProcurementRepository procurementRepository;
    private final AuditService auditService;

    private final ProcurementStatusUpdateRepository procurementStatusUpdateRepository;
    private final ProcurementSourceRepository procurementSourceRepository;


    public ProcurementServiceImpl(ProcurementMapper procurementMapper,
                                  UserRepository userRepository,
                                  ProcurementStatusRepository procurementStatusRepository,
                                  VendorRepository vendorRepository,
                                  RequestRepository requestRepository,
                                  AuthService authService,
                                  ProcurementRepository procurementRepository,
                                  AuditService auditService,
                                  ProcurementStatusUpdateRepository procurementStatusUpdateRepository,
                                  ProcurementSourceRepository procurementSourceRepository) {
        this.procurementMapper = procurementMapper;
        this.userRepository = userRepository;
        this.procurementStatusRepository = procurementStatusRepository;
        this.vendorRepository = vendorRepository;
        this.requestRepository = requestRepository;
        this.authService = authService;
        this.procurementRepository = procurementRepository;
        this.auditService = auditService;
        this.procurementStatusUpdateRepository = procurementStatusUpdateRepository;
        this.procurementSourceRepository = procurementSourceRepository;
    }

    @Override
    public List<ProcurementStatusDto> getProcurementStatusList() {
        //not sending the first status when updating the status
        return procurementStatusRepository.findAll()
                .stream()
//                .filter(status-> status.getId() != 2)
                .sorted(Comparator.comparing(ProcurementStatus::getName))
                .map(ProcurementStatus::getProcurementStatusDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<ProcurementSourceDto> getProcurementSources(){
        return procurementSourceRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(ProcurementSource::getName))
                .map(ProcurementSource::getdto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getProcurmentStagesList() {
        return Arrays.stream(ProcurementStage.values()).map(Enum::name).collect(Collectors.toList());
    }


    @Override
    public List<UserDto> getAssignedToUsersList() {
        //only users of supplies division must be in the list
        return userRepository.findAll()
                .stream()
                .filter(user-> user.getUserRole() == UserRole.SUPPLIESUSER)
                .sorted(Comparator.comparing(User::getEmail))
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VendorDto> getVendorsList() {
        return vendorRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Vendor::getName))
                .map(Vendor::getVendorDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProcurementResponseDto createProcurement(ProcurementCreateDto createDto) {
        //1. create procurement from dto -using mapper method
        Procurement procurement = procurementMapper.dtoToProcurement(createDto);

        //2. find objects from ids in dto
            //i. fetch assignedTo User- User

            User assignedTo = userRepository.findById(createDto.getAssignedToUserId())
                    .orElseThrow(() -> new RuntimeException("Assigned User not found"));
            //check if the user is a supplies user
            if(!assignedTo.getUserRole().equals(UserRole.SUPPLIESUSER)){
                throw new RuntimeException("Assigned user must be a supplies user");
            }

            //set
            procurement.setAssignedTo(assignedTo);

            //ii. fetch Vendor- Vendor
        if(createDto.getVendorId() != null){
            Vendor vendor = vendorRepository.findById(createDto.getVendorId())
                    .orElseThrow(()-> new RuntimeException("Vendor is not found"));
            //set
            procurement.setVendor(vendor);
        }
        //check for source
        ProcurementSource procurementSource = procurementSourceRepository.findById(createDto.getSourceId())
                    .orElseThrow(() -> new RuntimeException("Source is not found"));

        //set source
        procurement.setSource(procurementSource);



            //iii. assign Requests - List-Request
        //find if sent request list is empty
        if(createDto.getRequestId() == null ) {
            throw new RuntimeException("Request is empty");
        }
        //validate request ids - using class method
        Request validatedRequest= validateAsValidRequest(createDto.getRequestId(),true);
        //set
        procurement.setRequest(validatedRequest);


        //3.find and assign other backend objects
            //i. set procurement stage
        procurement.setProcurementStage(ProcurementStage.PROCUREMENT_PROCESS_NOT_COMMENCED);

            //ii. assign createdBy -User
        User loggedUser = getLoggedUser();
            //set
        procurement.setCreatedBy(loggedUser);

        procurement.setLastUpdatedBy(loggedUser);

            //iii. assign createdOn - Date
        procurement.setCreatedOn(new Date());

        //6.save Procurement to db
        Procurement savedProcurement = procurementRepository.save(procurement);


        //7. change requests status
        validatedRequest.setStatus(RequestStatus.PROCUREMENT_CREATED);
        requestRepository.save(validatedRequest);

        //8. Add to audit log
        auditService.log(
                loggedUser.getEmail(),
                loggedUser.getEmployeeId(),
                AuditAction.CREATED,
                AuditEntityType.PROCUREMENT,
                savedProcurement.getId(),
                "Created procurement with id: "+ savedProcurement.getId() +" including request ids: "
                        +createDto.getRequestId()+
                        " and vendor id: "+createDto.getVendorId()+
                        " and name of procurement: " + createDto.getName()
        );

        //7.return as dto
        return procurementMapper.toResponseDto(savedProcurement);


    }

    @Override
    public List<ProcurementResponseDto> getProcurement() {
         List<Procurement> procurements = procurementRepository.findAll();
         return procurementMapper.toResponseDtoList(procurements);
    }

    @Override
    public ProcurementResponseDto getProcurementById(Long id) {
        Procurement procurement = procurementRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Procurement not found"));
        return procurementMapper.toResponseDto(procurement);
    }

    @Override
    @Transactional
    public ProcurementResponseDto updateProcurement(Long id, ProcurementCreateDto createDto) {
        //find the procurement - using class method
        Procurement existingProcurement = validateToUpdateDeleteProcurement(id);

        //finding existing vendor name & id (for audit log)
        Vendor oldVendor = existingProcurement.getVendor();
        String oldVendorName = oldVendor.getName();
        Long oldVendorId = oldVendor.getId();

        //finding existing request id
        Long oldRequestId = existingProcurement.getRequest().getId();

        //5. find other objects from ids in dto
        //i. fetch new assignedTo - User
        if(createDto.getAssignedToUserId()!= null) {
            User assignedTo = userRepository.findById(createDto.getAssignedToUserId())
                    .orElseThrow(() -> new RuntimeException("Assigned User not found"));
            //check if the user is a supplies user
            if (!assignedTo.getUserRole().equals(UserRole.SUPPLIESUSER)) {
                throw new RuntimeException("Assigned user must be a supplies user");
            }

            //set
            existingProcurement.setAssignedTo(assignedTo);
        }

        //ii. fetch Vendor- Vendor
        if(createDto.getVendorId() != null){
            Vendor vendor = vendorRepository.findById(createDto.getVendorId())
                    .orElseThrow(()-> new RuntimeException("Vendor is not found"));
            //set
            existingProcurement.setVendor(vendor);
        }

        //iii. assign Request
        //find if sent request is null
        if(createDto.getRequestId() == null ) {
            throw new RuntimeException("Request is empty");
        }
        //validate request ids - using class method
        Request requestNew= validateAsValidRequest(createDto.getRequestId(),false);
        //if requests in the list has 'procurement created' then it should be equal to the one already in the procurement
        //i. get already procurement request list
        Request requestOld =  existingProcurement.getRequest();
        if(requestNew.getStatus().equals(RequestStatus.PROCUREMENT_CREATED)){
            if(!requestNew.getId().equals(oldRequestId)){
                throw new RuntimeException("Can not select requests in other procurement");
            }
        }


//        for(Request request: requestsNew){
//            if(request.getStatus().equals(RequestStatus.PROCUREMENT_CREATED)){
//                if(!requestsOld.contains(request)){
//                    throw new RuntimeException("Can not select requests in other procurement");
//                }
//            }
//        }
        //set
        existingProcurement.setRequest(requestNew);

        //4. update with details
        procurementMapper.updateProcurementWithDto(existingProcurement,createDto);


        //5. set updated details
        Long loggedUserId = authService.getLoggedUserDto().getId();
        User updatedBy = userRepository.findById(loggedUserId)
                .orElseThrow(()-> new RuntimeException("Current user is not found"));
        existingProcurement.setLastUpdatedBy(updatedBy);
        existingProcurement.setLastUpdatedOn(new Date());

        //6. save to db
        Procurement updatedProcurement = procurementRepository.save(existingProcurement);
        //save request status
        if(!requestNew.getId().equals(oldRequestId)){
            requestNew.setStatus(RequestStatus.PROCUREMENT_CREATED);
            requestOld.setStatus(RequestStatus.PENDING_PROCUREMENT);
            //save to db
            requestRepository.save(requestNew);
            requestRepository.save(requestOld);
        }

        //save requests with procurement number
//        for(Request request : requestsNew){
//            request.setStatus(RequestStatus.PROCUREMENT_CREATED);
//            request.setProcurement(updatedProcurement);
//        }
        //remove procurement and status from the removed requests of existing list
        //find removed request list
//        List<Request> removedRequests = new ArrayList<>();
//        for(Request request: requestsOld){
//            if(!requestsNew.contains(request)){
//                removedRequests.add(request);
//            }
//        }
        //change its request status and remove procurement
//        if(!removedRequests.isEmpty()) {
//            for (Request request : removedRequests){
//                request.setStatus(RequestStatus.PENDING_PROCUREMENT);
//                request.setProcurement(null);
//            }
//            //save to db
//            requestRepository.saveAll(removedRequests);
//        }
//
//        requestRepository.saveAll(requestsNew);

        //7.create audit log
        auditService.log(
                updatedBy.getEmail(),
                updatedBy.getEmployeeId(),
                AuditAction.UPDATED,
                AuditEntityType.PROCUREMENT,
                updatedProcurement.getId(),
                "Updated procurement with id:" +id + " included request ids: "+
                        oldRequestId + " and vendor id: " + oldVendorId + " and name: "+ oldVendorName +
                        " updated to include request ids: " +createDto.getRequestId()+
                        " and vendor id: "+createDto.getVendorId()

        );

        //8.return as response dto
        return procurementMapper.toResponseDto(updatedProcurement);


    }
    //for update form - we need request list with either approved or in procurement
    @Transactional
    @Override
    public List<RequestDto> getRequestsForUpdateProcurement() {
        return requestRepository.findAll()
                .stream()
                .filter(request ->
                request.getStatus().equals(RequestStatus.PROCUREMENT_CREATED)
                        || request.getStatus().equals(RequestStatus.PENDING_PROCUREMENT))
                .sorted(Comparator.comparing(Request::getId).reversed())
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }


    //on status update - create new object 'ProcurementStatusUpdate' & change status in 'Procurement'
    @Transactional
    @Override
    public ProcurementStatusUpdateDto updateStatus(Long procurementId, ProcurementStatusUpdateDto statusUpdateDto) {
        ProcurementStatus newStatus = null;

        //1. find procurement to update
        Procurement existingProcurement = procurementRepository.findById(procurementId)
                .orElseThrow(()-> new RuntimeException("Procurement is not found"));

        //2. to update the assigned user can not be null
        if(existingProcurement.getAssignedTo() == null){
            throw new RuntimeException("To update status procurement should have an assigned employee");
        }

        //3. Check if the user logged is allowed to update status
        //should be the one  assigned
        Long loggedUserId = authService.getLoggedUserDto().getId();
        //since assigned user could be null
        Long assignedUserId = existingProcurement.getAssignedTo().getId();
        if(!loggedUserId.equals(assignedUserId)){
            throw new RuntimeException("The procurement is not allowed to be updated by this user!");
        }

        //extra - check if procurement is already completed
        if(existingProcurement.getCompletedDate() != null){
            throw new RuntimeException("Can not update status as the procurement is already completed.");
        }

        //4. find the status
        //find stage selected
        if(statusUpdateDto.getProcurementStage() == null){
            throw new RuntimeException("The stage should be selected");
        }


        //save previous values for audit log before updating (for audit log)
        String previousStage = existingProcurement.getProcurementStage().toString();


    //taking care of status added
        if(statusUpdateDto.getProcurementStatusId() != null) {
            //if existing stage and status is selected again
            if(existingProcurement.getStatus() != null && existingProcurement.getStatus().getId().equals(statusUpdateDto.getProcurementStatusId())) {
                throw new RuntimeException("Can not select the same status.");
            }

            ProcurementStatus changedStatus = procurementStatusRepository.findById(statusUpdateDto.getProcurementStatusId())
                    .orElseThrow(() -> new RuntimeException("Status selected is not found"));
            //change
            existingProcurement.setStatus(changedStatus);
            //save to method variable
            newStatus = changedStatus;
        }else{
            //remove status if not added to update-status
            existingProcurement.setStatus(null);
        }


        //extra - check if the stage changed to completed - add completed date to procurement
        boolean isCompletedStage = statusUpdateDto.getProcurementStage().equals(ProcurementStage.PAID_AND_COMPLETED.toString());
        //set completed date of procurement if it is
        if(isCompletedStage){
            Date completedDate = statusUpdateDto.getStatusChangedOn() != null ?
                    statusUpdateDto.getStatusChangedOn() : new Date();
            existingProcurement.setCompletedDate(completedDate);
        }

        //extra - check if status is changed to commenced - add commenced date to procurement
        boolean isCommencedStage = statusUpdateDto.getProcurementStage().equals(ProcurementStage.PURCHASE_PROCESS_COMMENCED.toString());
        //set completed date of procurement if it is
        if(isCommencedStage){
            Date commencedDate = statusUpdateDto.getStatusChangedOn() != null ?
                    statusUpdateDto.getStatusChangedOn() : new Date();
            existingProcurement.setCommencedDate(commencedDate);
        }

        //5. find the updating user
        User updatedBy = userRepository.findById(loggedUserId)
                .orElseThrow(()-> new RuntimeException("Logged user not found"));


        //5.update the stage of procurement
       existingProcurement.setProcurementStage(ProcurementStage.valueOf(statusUpdateDto.getProcurementStage()));


        //6. create new statusUpdate object
        ProcurementStatusUpdate statusUpdate= new ProcurementStatusUpdate();
        statusUpdate.setComment(statusUpdateDto.getComment());
        //if date is given, set or set current date
        if(statusUpdateDto.getStatusChangedOn()== null){
            statusUpdate.setStatusChangedOn(new Date());
        }else{
            statusUpdate.setStatusChangedOn(statusUpdateDto.getStatusChangedOn());
        }
        //set createdOn
        statusUpdate.setCreatedOn(new Date());
        //if status is given
        if(newStatus != null) {
            statusUpdate.setStatus(newStatus);
        }
        statusUpdate.setProcurementStage(ProcurementStage.valueOf(statusUpdateDto.getProcurementStage()));
        statusUpdate.setProcurement(existingProcurement);
        statusUpdate.setCreatedBy(updatedBy);

        //set updated details in procurement
//        existingProcurement.setLastUpdatedOn(new Date());
//        existingProcurement.setLastUpdatedBy(updatedBy);

        //7. save both to db
        Procurement savedProcurement = procurementRepository.save(existingProcurement);
        ProcurementStatusUpdate savedStatusUpdate = procurementStatusUpdateRepository.save(statusUpdate);



        //7. add to audit log
        auditService.log(
                updatedBy.getEmail(),
                updatedBy.getEmployeeId(),
                AuditAction.STATUS_CHANGED,
                AuditEntityType.PROCUREMENT,
                savedProcurement.getId(),
                "Status of procurement " +procurementId + " updated  from stage: "+
                       previousStage + " to stage: " + savedStatusUpdate.getProcurementStage().toString() +
                       " with comments '"+
                        statusUpdateDto.getComment() + "'."

        );


        //8.return as response dto
        return savedStatusUpdate.getDto();
    }

    @Override
    public List<ProcurementStatusUpdateDto> getStatusUpdatesByProcurementId(Long procurementId) {
        return procurementStatusUpdateRepository.findAllByProcurementId(procurementId)
                .stream()
                .sorted(Comparator.comparing(ProcurementStatusUpdate::getCreatedOn).reversed())
                .map(ProcurementStatusUpdate::getDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void deleteProcurement(Long id) {
        Procurement existingProcurement = validateToUpdateDeleteProcurement(id);
        //update related request
        Request request = requestRepository.findById(existingProcurement.getRequest().getId())
                .orElseThrow(() -> new RuntimeException("Related request is not found"));
        //change status of request
        request.setStatus(RequestStatus.PENDING_PROCUREMENT);
        //save to db
        requestRepository.save(request);


        //find request objects
//        List<Long> requestIds = requests.stream().map(Request::getId).collect(Collectors.toList());
//        List<Request> existingRequests = requestRepository.findAllById(requestIds);
//        //update status of requests
//        for(Request request : existingRequests){
//            request.setStatus(RequestStatus.PENDING_PROCUREMENT);
//            request.setProcurement(null);
//        }
//        //save requests in db
//        requestRepository.saveAll(existingRequests);

        //delete procurement
        procurementRepository.deleteById(id);
        //create audit log
        User updatedBy = getLoggedUser();
        auditService.log(
                updatedBy.getEmail(),
                updatedBy.getEmployeeId(),
                AuditAction.DELETED,
                AuditEntityType.PROCUREMENT,
                id,
                "Deleted procurement including request id: "
                        + request.getId()+
                        " ,and title: "+existingProcurement.getName()
        );
    }

    private Procurement validateToUpdateDeleteProcurement(Long id){
        //1. find procurement to update
        Procurement existingProcurement = procurementRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Procurement is not found"));

        //2.check for correct stage (not completed)
        ProcurementStage stage = existingProcurement.getProcurementStage();
        if(stage.equals(ProcurementStage.PAID_AND_COMPLETED)) {
            throw new RuntimeException("This procurement can not be updated/deleted");
        }

        //3. Check if the user logged is allowed to update/delete
        //should be the one created procurement or assigned
        Long loggedUserId = authService.getLoggedUserDto().getId();
        //since assigned user could be null
        Long assignedUserId = existingProcurement.getAssignedTo()!= null ? existingProcurement.getAssignedTo().getId() : null;
        Long createdUserId = existingProcurement.getCreatedBy().getId();
        if(!loggedUserId.equals(assignedUserId) && !loggedUserId.equals(createdUserId)){
            throw new RuntimeException("The procurement is not allowed to be updated by this user!");
        }

        return existingProcurement;

    }


    private Request validateAsValidRequest(Long requestId, boolean isCreate){
        //find request object
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request selected is not found"));

//        if(requests.size() != requestIds.size()){
//            throw new RuntimeException("Some requests are not found");
//        }

        if(isCreate) {
            if(!request.getStatus().equals(RequestStatus.PENDING_PROCUREMENT)){
                throw new RuntimeException("Request is not ready for procurement");
            }

            //check for correct status of each request - should be pe
//            requests.stream()
//                    .filter(request -> !request.getStatus().equals(RequestStatus.PENDING_PROCUREMENT))
//                    .findFirst()
//                    .ifPresent(invalidRequest -> {
//                        throw new RuntimeException("Some requests are not ready for procurement");
//                    });
        }else{
            if(!request.getStatus().equals(RequestStatus.PENDING_PROCUREMENT) &&
            !(request.getStatus().equals(RequestStatus.PROCUREMENT_CREATED))){
                throw new RuntimeException("Request can not be added for procurement");
            }
            //check for 2 correct status of each request - the one already have should be allowed -
//            requests.stream()
//                    .filter(request ->
//                            !request.getStatus().equals(RequestStatus.PROCUREMENT_CREATED)
//                            && !request.getStatus().equals(RequestStatus.PENDING_PROCUREMENT))
//                    .findFirst()
//                    .ifPresent(invalidRequest -> {
//                        throw new RuntimeException("Some requests can not be added for a procurement.");
//                    });
        }

        return request;


//        //count ids sent
//        Long uniqueInputCount = requestIds.stream().distinct().count();
//        //count from db
//        Long dbCount = requestRepository.countByIdIn(requestIds);
//        //compare to find if its same
//        return uniqueInputCount  == dbCount;
}

private User getLoggedUser(){
        Long loggedUserId = authService.getLoggedUserDto().getId();
        //find in db
    User loggedUser = userRepository.findById(loggedUserId)
            .orElseThrow(() -> new RuntimeException("Logged user not found"));
    return loggedUser;
}

}
