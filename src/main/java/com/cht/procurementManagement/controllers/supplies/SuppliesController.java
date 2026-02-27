package com.cht.procurementManagement.controllers.supplies;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.services.requests.RequestService;
import com.cht.procurementManagement.services.supplies.SuppliesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplies")
@CrossOrigin("*")
public class SuppliesController {

    private final SuppliesService suppliesService;

    public SuppliesController( SuppliesService suppliesService) {

        this.suppliesService = suppliesService;
    }

    //getting all requests
    @GetMapping("/requests")
    public ResponseEntity<List<RequestDto>> getAllRequests(){
        return ResponseEntity.ok(suppliesService.getAllRequests());
    }

    //requests to be reviewed by supplies
    @GetMapping("/requests/review")
    public ResponseEntity<List<RequestDto>> getReviewRequestsBySupplies(){
        return ResponseEntity.ok(suppliesService.getRequestsPendingSuppliesApproval());
    }

    //requests approved by supplies -pending procurement
    @GetMapping("/requests/approved")
    public ResponseEntity<List<RequestDto>> getApprovedRequestsBySupplies(){
        return ResponseEntity.ok(suppliesService.getRequestsApproved());
    }

    //get comments list
    @GetMapping("/requests/comments/{id}")
    public ResponseEntity<?> getCommentsByRequestId(@PathVariable Long id){
        try {
            return ResponseEntity.ok(suppliesService.getCommentsByRequestId(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //get approvals list
    @GetMapping("/requests/approvals/{id}")
    public ResponseEntity<?> getApprovalsByRequestId(@PathVariable Long id){
        try {
            return ResponseEntity.ok(suppliesService.getApprovalsByRequestId(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get request by id
    @GetMapping("/requests/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(suppliesService.getRequestById(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




    //create request by supplies
    @PostMapping("/requests")
    public ResponseEntity<?> createRequest(@RequestBody RequestDto requestDto){
        RequestDto createdRequestDto = suppliesService.createRequestBySupplies(requestDto);
        if(createdRequestDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request couldn't be created.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestDto);
    }

    //reject requests
    @PostMapping("/requests/reject/{id}")
    public ResponseEntity<?> rejectRequestBySupplies(@PathVariable Long id, @RequestBody CommentDto commentDto){
        CommentDto createdCommentDto = suppliesService.rejectRequestBySupplies(id, commentDto);
        if(createdCommentDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comment couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDto);
    }

    //approve requests
    @PostMapping("/requests/approve/{id}")
    public ResponseEntity<?> approveRequestBySupplies(@PathVariable Long id, @RequestBody ApprovalDto approvalDto){
        ApprovalDto createdApprovalDto = suppliesService.approveRequestBySupplies(id, approvalDto);
        if(createdApprovalDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Approval couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApprovalDto);
    }

    @PutMapping("/requests/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Long id, @RequestBody RequestDto requestDto){
        RequestDto updatedRequestDto = suppliesService.updateRequest(id, requestDto);
        if(updatedRequestDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request couldn't be updated.");
        }
        return ResponseEntity.ok(updatedRequestDto);
    }

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id){
        try {
            suppliesService.deleteRequest(id);
            return ResponseEntity.ok(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/subdivs/admindiv/{id}")
    public ResponseEntity<List<SubdivDto>> getSubdivsByAdmindivId(@PathVariable  Long id){
        return ResponseEntity.ok(suppliesService.getSubdivsByAdmindivId(id));
    }

    //get all subdivs
    //get subdiv list
    @GetMapping("/grouped-subdivs")
    public ResponseEntity<?> getGroupedSubdivList(){
        try{
            return ResponseEntity.ok(suppliesService.getGroupedSubdivs());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get subdiv list
    @GetMapping("/subdivs")
    public ResponseEntity<List<SubdivDto>> getSubdivs(){
        return ResponseEntity.ok(suppliesService.getSubdivs());
    }


    //get admin div list
    @GetMapping("/admindivs")
    public ResponseEntity<List<AdmindivDto>> getAdmindivs(){
        return ResponseEntity.ok(suppliesService.getAllAdmindivs());    }



}
