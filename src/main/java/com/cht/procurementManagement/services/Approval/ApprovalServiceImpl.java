package com.cht.procurementManagement.services.Approval;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.entities.Approval;
import com.cht.procurementManagement.entities.Request;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.repositories.ApprovalRepository;
import com.cht.procurementManagement.repositories.RequestRepository;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApprovalServiceImpl implements  ApprovalService{
    private final AuthService authService;
    private final ApprovalRepository approvalRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    public ApprovalServiceImpl(AuthService authService,
                               ApprovalRepository approvalRepository,
                               RequestRepository requestRepository,
                               UserRepository userRepository) {
        this.authService = authService;
        this.approvalRepository = approvalRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApprovalDto createApproval(ApprovalDto approvalDto) {
        //setting only createdBy user & created Date here
        //set type & request before sending here

        //get user id from logged user details
        Long loggedUserId = authService.getLoggedUserDto().getId();
        //finding the User object from db
        User userCreatedBy = userRepository.findById(loggedUserId)
                .orElseThrow(()-> new RuntimeException("User not found!"));

        //finding the relevant request in db from request id
        Optional<Request> optionalRequest = requestRepository.findById(approvalDto.getRequestId());
        if(optionalRequest.isPresent()){
            //create approval object & set details from dto
            Approval approval = new Approval();
            approval.setAllocatedAmount(approvalDto.getAllocatedAmount());
            approval.setAmountInWords(approvalDto.getAmountInWords());
            approval.setPlanNo(approvalDto.getPlanNo());
            approval.setComment(approvalDto.getComment());
            approval.setFund(approvalDto.getFund());
            approval.setAuthroizedBy(approvalDto.getAuthorizedBy());
            approval.setApprovedDate(approvalDto.getApprovedDate());
            //set current date
            approval.setCreatedDate(new Date());
            approval.setType(approvalDto.getType());
            //user
            approval.setCreatedBy(userCreatedBy);
            //request
            approval.setRequest(optionalRequest.get());
            //save to db & return as dto
            return approvalRepository.save(approval).getApprovalDto();
        }else{
            throw new EntityNotFoundException("Request is not found");
        }

    }

    @Override
    public List<ApprovalDto> getApprovalsByRequestId(Long requestId) {
       return approvalRepository.findAllByRequestId(requestId)
               .stream()
               .sorted(Comparator.comparing(Approval::getId))
               .map(Approval::getApprovalDto)
               .collect(Collectors.toList());

    }
}
