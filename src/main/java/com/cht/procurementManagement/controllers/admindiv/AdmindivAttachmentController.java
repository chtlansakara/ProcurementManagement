package com.cht.procurementManagement.controllers.admindiv;

import com.cht.procurementManagement.entities.PDFAttachment;
import com.cht.procurementManagement.services.admindiv.AdminDivService;
import com.cht.procurementManagement.services.attachment.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/admindiv")
@CrossOrigin("*")
public class AdmindivAttachmentController {
    private final AdminDivService adminDivService;
    private final AttachmentService attachmentService;

    public AdmindivAttachmentController(AdminDivService adminDivService,
                                        AttachmentService attachmentService) {
        this.adminDivService = adminDivService;
        this.attachmentService = attachmentService;
    }


    //get request attachment by request id
    @GetMapping("/request-attachment/{id}")
    public ResponseEntity<?> getRequestAttachment(@PathVariable Long id){
        Optional<PDFAttachment> attachment = adminDivService.getAdmindivRequestAttachment(id);
        if(attachment.isPresent()){
            return ResponseEntity.ok().body(attachment);
        }
        return ResponseEntity.noContent().build();
    }

    //get approval attachment by approval id
    @GetMapping("/approval-attachment/{id}")
    public ResponseEntity<?> getApprovalAttachment(@PathVariable Long id){

            Optional<PDFAttachment> attachment = adminDivService.getAdmindivApprovalAttachment(id);
            if(attachment.isPresent()){
                return ResponseEntity.ok().body(attachment);
            }
            return ResponseEntity.noContent().build();
    }

    //download resources (request and approval attachments only)
    @GetMapping("/procurement-download/{fileId}")
    public ResponseEntity<Resource> downloadProcurementAttachment(@PathVariable Long fileId){
            PDFAttachment attachment = attachmentService.getAttachmentById(fileId);
            Resource resource = adminDivService.downloadAdmindivAttachment(fileId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getOriginalName() + "\"")
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .contentLength(attachment.getFileSize())
                    .body(resource);
    }

    //for request file update
    @PostMapping("/request-upload/{id}")
    public ResponseEntity<?> uploadRequestAttachment(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        try{
            PDFAttachment savedAttachment = adminDivService.uploadRequestAttachment(file, id);
            return ResponseEntity.ok().body(savedAttachment);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //for request file delete
    @DeleteMapping("/request-attachment/{fileId}")
    public ResponseEntity<?> deleteRequestAttachment(@PathVariable Long fileId) {
        adminDivService.deleteRequestAttachment(fileId);
        return ResponseEntity.ok(null);

    }
}
