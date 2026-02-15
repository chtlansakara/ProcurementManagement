package com.cht.procurementManagement.controllers.supplies;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.RequestDto;
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
    @PostMapping("requests/approve/{id}")
    public ResponseEntity<?> approveRequestBySupplies(@PathVariable Long id, @RequestBody ApprovalDto approvalDto){
        ApprovalDto createdApprovalDto = suppliesService.approveRequestBySupplies(id, approvalDto);
        if(createdApprovalDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Approval couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApprovalDto);
    }
}
