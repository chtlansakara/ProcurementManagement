package com.cht.procurementManagement.controllers.subdiv;

import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.services.Comment.CommentService;
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


    public SubdivUserController(SubDivService subDivService
                              ) {
        this.subDivService = subDivService;

    }


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
