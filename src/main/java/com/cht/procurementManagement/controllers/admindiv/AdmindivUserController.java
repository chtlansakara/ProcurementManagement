package com.cht.procurementManagement.controllers.admindiv;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.services.admindiv.AdminDivService;
import com.cht.procurementManagement.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admindiv")
@CrossOrigin("*")
public class AdmindivUserController {

    private final AdminDivService adminDivService;

    private final AuthService authService;

    public AdmindivUserController(AdminDivService adminDivService, AuthService authService) {
        this.adminDivService = adminDivService;
        this.authService = authService;
    }

    //get ONLY admin div requests
    @GetMapping("/requests")
    public ResponseEntity<?> getRequestsOnlyByAdmindiv(){
        try{
            return ResponseEntity.ok(adminDivService.getAllRequestsOnlyByAdmindivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get all RELATED admin div requests
    @GetMapping("/requests/related")
    public ResponseEntity<?> getRelatedRequestsByAdmindiv(){
        try{
            return ResponseEntity.ok(adminDivService.getAllRequestsRelatedByAdmindivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get requests pending admin approval
    @GetMapping("/requests/review")
    public ResponseEntity<?> getReviewRequestsByAdmindiv(){
        try{
            return ResponseEntity.ok(adminDivService.getRequestsPendingAdmindivApproval());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //create request by admin div
    @PostMapping("/requests")
    public ResponseEntity<?> createRequestByAdmindiv(@RequestBody RequestDto requestDto){
        //save
        RequestDto createdRequestDto = adminDivService.createRequestByAdmindiv(requestDto);
        if(createdRequestDto==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestDto);
    }


    //reject the request
    @PostMapping("/requests/reject/{id}")
    public ResponseEntity<?> rejectRequestByAdmindiv(@PathVariable Long id, @RequestBody CommentDto commentDto){
        CommentDto createdCommentDto = adminDivService.rejectRequestByAdmindiv(id,commentDto);
        if(createdCommentDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comment couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDto);
    }

    //approve the request
    @PostMapping("requests/approve/{id}")
    public ResponseEntity<?> approveRequestBySupplies(@PathVariable Long id, @RequestBody ApprovalDto approvalDto){
        ApprovalDto createdApprovalDto =adminDivService.approveRequestByAdmindiv(id, approvalDto);
        if(createdApprovalDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Approval couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApprovalDto);
    }



}
