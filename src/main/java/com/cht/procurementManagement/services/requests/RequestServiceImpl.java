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

        //only the created user & created date is set here
        //set the status & sub-div id list before sending here

        //finding the logged user id
        Long loggedUserId = authService.getLoggedUserDto().getId();

        //finding User object from db
        User userCreatedBy  = userRepository.findById(loggedUserId)
                .orElseThrow( () -> new RuntimeException("User not found!"));

            //finding the sub div objects list from db
            List<Subdiv> subdivList = subdivRepository.findAllById(requestDto.getSubdivIdList());

            //create a new request
            Request request = new Request();

            request.setTitle(requestDto.getTitle());
            request.setQuantity(requestDto.getQuantity());
            request.setDescription(requestDto.getDescription());
            request.setFund(requestDto.getFund());
            request.setEstimation(requestDto.getEstimation());
            //already have the status
            request.setStatus(requestDto.getStatus());
            request.setPreviouslyPurchased(requestDto.isPreviouslyPurchased());
            request.setPreviousPurchaseYear(requestDto.getPreviousPurchaseYear());
            request.setReasonForRequirement(requestDto.getReasonForRequirement());
            request.setApprovedDate(requestDto.getApprovedDate());
            request.setAuthorizedBy(requestDto.getAuthorizedBy());
            //add current date as created date
            request.setCreatedDate(new Date());

            //setting objects User & Subdiv Object list
            request.setCreatedBy(userCreatedBy);
            request.setSubdivList(subdivList);

            //save
            Request savedRequest = requestRepository.save(request);
            return savedRequest.getRequestDto();
    }

    @Transactional
    @Override
    public RequestDto updateRequest(Long id, RequestDto requestDto) {
      //check for correct sub div list & status before here

       //check for request
        Optional<Request> optionalRequest = requestRepository.findById(id);

        //finding the logged user id
        Long loggedUserId = authService.getLoggedUserDto().getId();

        //finding User object from db
        User userUpdatedBy  = userRepository.findById(loggedUserId)
                .orElseThrow( () -> new RuntimeException("User not found!"));

        //finding the sub div objects list from db
        List<Subdiv> subdivList = subdivRepository.findAllById(requestDto.getSubdivIdList());

        if(optionalRequest.isPresent() && !subdivList.isEmpty()){
            //get Request object to update
            Request existingRequest = optionalRequest.get();

            //update with data
            existingRequest.setTitle(requestDto.getTitle());
            existingRequest.setQuantity(requestDto.getQuantity());
            existingRequest.setDescription(requestDto.getDescription());
            existingRequest.setFund(requestDto.getFund());
            existingRequest.setEstimation(requestDto.getEstimation());
            //already have the status
            existingRequest.setStatus(requestDto.getStatus());
            existingRequest.setPreviouslyPurchased(requestDto.isPreviouslyPurchased());
            existingRequest.setPreviousPurchaseYear(requestDto.getPreviousPurchaseYear());
            existingRequest.setReasonForRequirement(requestDto.getReasonForRequirement());
            existingRequest.setApprovedDate(requestDto.getApprovedDate());
            existingRequest.setAuthorizedBy(requestDto.getAuthorizedBy());
            //add current date as created date
            existingRequest.setCreatedDate(new Date());

            //setting objects User & Subdiv Object list
            existingRequest.setCreatedBy(userUpdatedBy);
            existingRequest.setSubdivList(subdivList);

            //save and return as dto
            return requestRepository.save(existingRequest).getRequestDto();
        }

        return null;
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

}
