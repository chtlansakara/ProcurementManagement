package com.cht.procurementManagement.services.requests;

import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.repositories.RequestRepository;
import com.cht.procurementManagement.repositories.SubdivRepository;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements  RequestService{
    //need User, Sub-div, Admin-div repositories
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final SubdivRepository subdivRepository;
    private final AuthService authService;

    public RequestServiceImpl(RequestRepository requestRepository,
                              UserRepository userRepository,
                              SubdivRepository subdivRepository,
                              AuthService authService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.subdivRepository = subdivRepository;

        this.authService = authService;
    }

    @Transactional
    //create request method with all options
    @Override
    public RequestDto createRequest(RequestDto requestDto) {
    //set 'status' and 'subdivIdList' of dto before here
        //only the created user & created date is set here

        //1. set 'createdBy' user -
        //finding the logged user id
        Long loggedUserId = authService.getLoggedUserDto().getId();
        //finding User object from db
        User userCreatedBy  = userRepository.findById(loggedUserId)
                .orElseThrow( () -> new RuntimeException("User not found!"));

        //2. find subdiv objects from db
        //finding the sub div objects list from db
        List<Subdiv> subdivList = subdivRepository.findAllById(requestDto.getSubdivIdList());

        //3. create a new request
        Request request = new Request();

        request.setTitle(requestDto.getTitle());
        request.setQuantity(requestDto.getQuantity());
        request.setDescription(requestDto.getDescription());
        request.setFund(requestDto.getFund());
        request.setEstimation(requestDto.getEstimation());

        //should already have the status in dto
        request.setStatus(requestDto.getStatus());

        request.setPreviouslyPurchased(requestDto.isPreviouslyPurchased());
        request.setPreviousPurchaseYear(requestDto.getPreviousPurchaseYear());
        request.setReasonForRequirement(requestDto.getReasonForRequirement());
        request.setApprovedDate(requestDto.getApprovedDate());
        request.setAuthorizedBy(requestDto.getAuthorizedBy());

        //add current date as created date
        request.setCreatedDate(new Date());

        //setting objects - User & Subdiv Objects list
        request.setCreatedBy(userCreatedBy);
        request.setSubdivList(subdivList);

        //save to db & return as dto
        Request savedRequest = requestRepository.save(request);
        return savedRequest.getRequestDto();
    }

    @Transactional
    @Override
    public RequestDto updateRequest(Long id, RequestDto requestDto) {
      //check for correct sub div list & status in dto before here

       //check for request
        Optional<Request> optionalRequest = requestRepository.findById(id);

        //finding the logged user id
        Long loggedUserId = authService.getLoggedUserDto().getId();

        //finding User object from db
        User userUpdatedBy  = userRepository.findById(loggedUserId)
                .orElseThrow( () -> new RuntimeException("User not found!"));

        //check if sub div is empty
        if(requestDto.getSubdivIdList() == null || requestDto.getSubdivIdList().contains(null)){
            throw new RuntimeException("Sub divisions list must not be empty or contain null values");
        }

        //finding the sub div objects list from db
            List<Subdiv> subdivList = subdivRepository.findAllById(requestDto.getSubdivIdList());

        if(optionalRequest.isPresent()){
            //get Request object to update
            Request existingRequest = optionalRequest.get();

            //update with data
            existingRequest.setTitle(requestDto.getTitle());
            existingRequest.setQuantity(requestDto.getQuantity());
            existingRequest.setDescription(requestDto.getDescription());
            existingRequest.setFund(requestDto.getFund());
            existingRequest.setEstimation(requestDto.getEstimation());


            existingRequest.setStatus(requestDto.getStatus());

            existingRequest.setPreviouslyPurchased(requestDto.isPreviouslyPurchased());
            existingRequest.setPreviousPurchaseYear(requestDto.getPreviousPurchaseYear());
            existingRequest.setReasonForRequirement(requestDto.getReasonForRequirement());
            existingRequest.setApprovedDate(requestDto.getApprovedDate());
            existingRequest.setAuthorizedBy(requestDto.getAuthorizedBy());

            //add current date as created date
            existingRequest.setCreatedDate(new Date());

            //setting objects User & Sub-div Object list
            existingRequest.setCreatedBy(userUpdatedBy);
            //set only if the new list is not empty - if empty doesn't change

                existingRequest.setSubdivList(subdivList);


            //save and return as dto
            return requestRepository.save(existingRequest).getRequestDto();
        }
        return null;
    }

    //USED ONE
    @Override
    public RequestDto updateRequest(Request request, RequestDto requestDto) {

        //4. get logged User object
        Long loggedUserId = authService.getLoggedUserDto().getId();
        User updatedBy = userRepository.findById(loggedUserId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        //5. find sub-divs
        List<Subdiv> subdivList = subdivRepository.findAllById(requestDto.getSubdivIdList());

        //check for sub-div list
        if(subdivList.size() != requestDto.getSubdivIdList().size()){
            throw new RuntimeException("Invalid sub-div ids");
        }


        //Update fields of request with dto
        request.setTitle(requestDto.getTitle());
        request.setQuantity(requestDto.getQuantity());
        request.setDescription(requestDto.getDescription());
        request.setFund(requestDto.getFund());
        request.setEstimation(requestDto.getEstimation());

        request.setPreviouslyPurchased(requestDto.isPreviouslyPurchased());
        request.setPreviousPurchaseYear(requestDto.getPreviousPurchaseYear());
        request.setReasonForRequirement(requestDto.getReasonForRequirement());
        request.setApprovedDate(requestDto.getApprovedDate());
        request.setAuthorizedBy(requestDto.getAuthorizedBy());

        request.setStatus(requestDto.getStatus());

        //add current date as created date
        request.setLastUpdatedDate(new Date());
        //setting objects User & Sub-div Object list
        request.setLastUpdatedBy(updatedBy);

        request.setSubdivList(subdivList);

        return requestRepository.save(request).getRequestDto();
    }

    //get Requests by id
    @Override
    public RequestDto getRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .map(Request::getRequestDto)
                .orElseThrow(() -> new EntityNotFoundException("Request is not found"));
    }

    //get all requests - sorted - FYI
    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequests() {
        return requestRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Request::getCreatedDate).reversed())
                .map(Request::getRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRequest(Long requestId) {
        Optional<Request> optionalRequest = requestRepository.findById(requestId);

        if(!optionalRequest.isPresent()){
            throw new RuntimeException("Request is not found");
        }
        requestRepository.deleteById(requestId);
    }

    //USED ONE
    @Override
    public void deleteRequest(Request request) {
        requestRepository.delete(request);
    }


}
