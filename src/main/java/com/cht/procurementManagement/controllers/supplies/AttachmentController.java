package com.cht.procurementManagement.controllers.supplies;

import com.cht.procurementManagement.entities.PDFAttachment;
import com.cht.procurementManagement.enums.EntityType;
import com.cht.procurementManagement.services.attachment.AttachmentService;
import com.cht.procurementManagement.services.procurement.ProcurementService;
import com.cht.procurementManagement.services.supplies.SuppliesService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/supplies")
@CrossOrigin("*")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final ProcurementService procurementService;
    private final SuppliesService suppliesService;

    public AttachmentController(AttachmentService attachmentService,
                                ProcurementService procurementService,
                                SuppliesService suppliesService) {
        this.attachmentService = attachmentService;
        this.procurementService = procurementService;
        this.suppliesService = suppliesService;
    }

    //upload endpoints:
    //for procurement
    @PostMapping("/procurement-upload/{id}")
    public ResponseEntity<?> uploadProcurementAttachment(@PathVariable Long id,  @RequestParam String name, @RequestPart("file")MultipartFile file){
        try{
            PDFAttachment savedAttachment =procurementService.uploadProcurementAttachment(file, name, id);
            return ResponseEntity.ok().body(savedAttachment);
        } catch (IOException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //for request file update
    @PostMapping("/request-upload/{id}")
    public ResponseEntity<?> uploadRequestAttachment(@PathVariable Long id, @RequestParam("file")MultipartFile file){
        try{
            PDFAttachment savedAttachment = suppliesService.uploadRequestAttachment(file, id);
            return ResponseEntity.ok().body(savedAttachment);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //for request file delete
    @DeleteMapping("/request-attachment/{fileId}")
    public ResponseEntity<?> deleteRequestAttachment(@PathVariable Long fileId){
        try{
            suppliesService.deleteRequestAttachment(fileId);
            return ResponseEntity.ok(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //for approval upload (update) - not used
    @PostMapping("/approval-upload/{id}")
    public ResponseEntity<?> uploadApprovalAttachment(@PathVariable Long id,  @RequestParam ("file")MultipartFile file){
        try{
            PDFAttachment savedAttachment = attachmentService.uploadFile(file, "Approval", id,  EntityType.APPROVAL);
            return ResponseEntity.ok().body(savedAttachment);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    //download resources
    @GetMapping("/procurement-download/{fileId}")
    public ResponseEntity<Resource> downloadProcurementAttachment(@PathVariable Long fileId){
        try{
            PDFAttachment attachment = attachmentService.getAttachmentById(fileId);
            Resource resource = attachmentService.getFileResource(fileId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getOriginalName() + "\"")
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .contentLength(attachment.getFileSize())
                    .body(resource);
        } catch (IOException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //delete selected procurement file
    @DeleteMapping("/procurement-attachment/{fileId}")
    public ResponseEntity<?> deleteProcurementAttachment(@PathVariable Long fileId){
        try{
            procurementService.deleteProcurementAttachment(fileId);
            return ResponseEntity.ok(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //get all procurement attachments by procurement id
    @GetMapping("/procurement-attachments/{id}")
    public ResponseEntity<?> getAllProcurementAttachment(@PathVariable Long id){
        List<PDFAttachment> attachmentsList = attachmentService.getAllAttachmentsByProcurement(id);
        return ResponseEntity.ok().body(attachmentsList);

    }

    //get request attachment by request id
    @GetMapping("/request-attachment/{id}")
    public ResponseEntity<?> getRequestAttachment(@PathVariable Long id){
            Optional<PDFAttachment> attachment = attachmentService.getAttachment(id, EntityType.REQUEST);
            if(attachment.isPresent()){
                return ResponseEntity.ok().body(attachment);
            }
            return ResponseEntity.noContent().build();
    }

    //get approval attachment by approval id
    @GetMapping("/approval-attachment/{id}")
    public ResponseEntity<?> getApprovalAttachment(@PathVariable Long id){
            Optional<PDFAttachment> attachment = attachmentService.getAttachment(id, EntityType.APPROVAL);
            if(attachment.isPresent()){
                return ResponseEntity.ok().body(attachment);
            }
            return ResponseEntity.noContent().build();
    }
}
