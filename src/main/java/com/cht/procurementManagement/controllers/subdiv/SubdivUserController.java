package com.cht.procurementManagement.controllers.subdiv;

import com.cht.procurementManagement.dto.ProcurementStatusDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.services.procurement.ProcurementService;
import com.cht.procurementManagement.services.subdiv.SubDivService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subdiv")
@CrossOrigin("*")
public class SubdivUserController {
    private final SubDivService subDivService;
    private final ProcurementService procurementService;


    public SubdivUserController(SubDivService subDivService,
                                ProcurementService procurementService) {
        this.subDivService = subDivService;

        this.procurementService = procurementService;
    }

    //get procurement of subdiv
    @GetMapping("/procurement")
    public ResponseEntity<?> getProcurementOfSubdiv(){
        try {
            return ResponseEntity.ok(subDivService.getAllProcurementOnlyBySubdivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get procurement by id
    @GetMapping("/procurement/{id}")
    public ResponseEntity<?> getProcurementOfSubdivById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(subDivService. getProcurementByIdForSubdiv(id));
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


    //get all RELATED sub-div requests
    @GetMapping("/requests/related")
    public ResponseEntity<?> getRelatedRequestsBySubdiv(){
        try {
            return ResponseEntity.ok(subDivService.getAllRequestsRelatedBySubdivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get only all single sub-div requests
    @GetMapping("/requests")
    public ResponseEntity<?> getRequestsOnlyBySubdiv(){
        try {
            return ResponseEntity.ok(subDivService.getAllRequestsOnlyBySubdivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //create request for sub-div
    @PostMapping("/requests")
    public ResponseEntity<?> createRequestForSubdiv(@RequestBody RequestDto requestDto){
        //save to db
        RequestDto createdRequestDto = subDivService.createRequestBySubdiv(requestDto);
        if(createdRequestDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestDto);
    }

    //get request by id
    @GetMapping("/requests/{id}")
    public ResponseEntity<?> findRequestById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(subDivService.getRequestById(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get sub div by id
    @GetMapping("/subdiv-info")
    public ResponseEntity<?> findSubdivById(){
        try {
            return ResponseEntity.ok(subDivService.getSubdiv());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get comments by request id
    @GetMapping("/requests/comments/{id}")
    public ResponseEntity<?> getCommentsByRequestId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(subDivService.getCommentsByRequestId(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get approval by request id
    @GetMapping("/requests/approvals/{id}")
    public ResponseEntity<?> getApprovalsByRequestId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(subDivService.getApprovalsByRequestId(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //delete request
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id){
        try {
            subDivService.deleteRequestById(id);
            return ResponseEntity.ok(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //update request
    @PutMapping("/requests/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Long id, @RequestBody RequestDto requestDto){
        RequestDto updatedRequestDto = subDivService.updateRequestById(id, requestDto);
        if(updatedRequestDto ==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRequestDto);
    }

}
