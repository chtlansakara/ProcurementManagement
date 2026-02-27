package com.cht.procurementManagement.controllers.admindiv;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.services.admindiv.AdminDivService;
import com.cht.procurementManagement.services.auth.AuthService;
import com.cht.procurementManagement.services.procurement.ProcurementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admindiv")
@CrossOrigin("*")
public class AdmindivUserController {

    private final AdminDivService adminDivService;

    private final AuthService authService;
    private final ProcurementService procurementService;

    public AdmindivUserController(AdminDivService adminDivService,
                                  AuthService authService,
                                  ProcurementService procurementService) {
        this.adminDivService = adminDivService;
        this.authService = authService;
        this.procurementService = procurementService;
    }

    //get procurement of admindiv
    @GetMapping("/procurement")
    public ResponseEntity<?> getProcurementOfAdmindiv(){
        try {
            return ResponseEntity.ok(adminDivService.getAllProcurementOnlyByAdmindivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get procurement by id
    @GetMapping("/procurement/{id}")
    public ResponseEntity<?> getProcurementOfAdmindivById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(adminDivService. getProcurementByIdForAdmindiv(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get procurement status list
    @GetMapping("/procurement-status")
    public ResponseEntity<List<ProcurementStatusDto>> getProcurementStatus(){

        return ResponseEntity.ok(procurementService.getProcurementStatusList());
    }

    //get requests list for procurement
    @GetMapping("/procurement-requests")
    public ResponseEntity<List<RequestDto>> getRequestsForUpdateProcurement(){
        return ResponseEntity.ok(procurementService.getRequestsForUpdateProcurement());
    }

    //get status -updates for a procurement
    @GetMapping("/procurement-status/{id}")
    public ResponseEntity<?> getStatusUpdates(@PathVariable Long id){
        return ResponseEntity.ok(procurementService.getStatusUpdatesByProcurementId(id));
    }

    //get-stages list for filter
    @GetMapping("/procurement-stages")
    public ResponseEntity<List<String>> getProcurementStages(){
        return ResponseEntity.ok(procurementService.getProcurmentStagesList());
    }


//    request related.......


    //get ONLY admin div requests
    @GetMapping("/requests")
    public ResponseEntity<?> getRequestsOnlyByAdmindiv(){
        try{
            return ResponseEntity.ok(adminDivService.getAllRequestsOnlyByAdmindivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get request by id
    @GetMapping("/requests/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(adminDivService.getRequestByRequestId(id));
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

    //get requests approved by admin div
    @GetMapping("/requests/approved")
    public ResponseEntity<?> getApprovedRequestsByAdmindiv(){
        try{
            return ResponseEntity.ok(adminDivService.getRequestsApprovedByAdmindiv());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get requests rejected by admin div
    @GetMapping("/requests/rejected")
    public ResponseEntity<?> getRejectedRequestsByAdmindiv(){
        try{
            return ResponseEntity.ok(adminDivService.getRequestsRejectedByAdmindiv());
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
    @PostMapping("/requests/approve/{id}")
    public ResponseEntity<?> approveRequestBySupplies(@PathVariable Long id, @RequestBody ApprovalDto approvalDto){
        ApprovalDto createdApprovalDto =adminDivService.approveRequestByAdmindiv(id, approvalDto);
        if(createdApprovalDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Approval couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApprovalDto);
    }


    //get subdiv list
    @GetMapping("/subdivs")
    public ResponseEntity<?> getSubdivList(){
        try{
            return ResponseEntity.ok(adminDivService.getSubdivList());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get comments list
    @GetMapping("/requests/comments/{id}")
    public ResponseEntity<?> getCommentsByRequestId(@PathVariable Long id){
        try {
            return ResponseEntity.ok(adminDivService.getCommentsByRequestId(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //get approvals list
    @GetMapping("/requests/approvals/{id}")
    public ResponseEntity<?> getApprovalsByRequestId(@PathVariable Long id){
        try {
            return ResponseEntity.ok(adminDivService.getApprovalsByRequestId(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //update request
    @PutMapping("/requests/{id}")
    public ResponseEntity<?> updateRequestByRequestId(@PathVariable Long id, @RequestBody RequestDto requestDto){
        RequestDto updatedRequestDto = adminDivService.updateRequestByRequestId(id, requestDto);
        if(updatedRequestDto== null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request couldn't be updated");
        }
        return ResponseEntity.ok(updatedRequestDto);
    }

    //delete request
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id){
        try {
            adminDivService.deleteRequest(id);
            return ResponseEntity.ok(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
