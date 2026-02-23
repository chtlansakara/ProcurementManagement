package com.cht.procurementManagement.controllers.supplies;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.dto.procurement.ProcurementCreateDto;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.dto.procurement.ProcurementStatusUpdateDto;
import com.cht.procurementManagement.services.AuditLog.AuditLogService;
import com.cht.procurementManagement.services.procurement.ProcurementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplies")
@CrossOrigin("*")
public class ProcurementController {
    private final ProcurementService procurementService;
    private final AuditLogService auditLogService;

    public ProcurementController(ProcurementService procurementService,
                                 AuditLogService auditLogService) {
        this.procurementService = procurementService;
        this.auditLogService = auditLogService;
    }

    //create procurement
    @PostMapping("/procurement")
    public ResponseEntity<?> createProcurement(@RequestBody ProcurementCreateDto createDto){
        ProcurementResponseDto createdProcurementResponseDto = procurementService.createProcurement(createDto);
        if(createdProcurementResponseDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Procurement couldn't be created.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProcurementResponseDto);
    }

    @PutMapping("/procurement/{id}")
    public ResponseEntity<?> updateProcurement(@PathVariable Long id, @RequestBody ProcurementCreateDto createDto){
        ProcurementResponseDto updatedDto = procurementService.updateProcurement(id,createDto);
        if(updatedDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Procurement couldn't be updated");
        }
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/procurement/{id}")
    public ResponseEntity<?> deleteProcurement(@PathVariable Long id){
        try {
            procurementService.deleteProcurement(id);
            return ResponseEntity.ok(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/procurement")
    public ResponseEntity<List<ProcurementResponseDto>> getAll(){
        return ResponseEntity.ok(procurementService.getProcurement());
    }

    @GetMapping("/procurement/{id}")
    public ResponseEntity<?> getProcurementById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(procurementService.getProcurementById(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //change status & create update-status object for a procurement
    @PutMapping("/procurement/status/{id}")
    public ResponseEntity<?> updateProcurementStatus(@PathVariable Long id, @RequestBody ProcurementStatusUpdateDto statusUpdateDto){
        ProcurementStatusUpdateDto updatedStatusDto = procurementService.updateStatus(id, statusUpdateDto);
        if(updatedStatusDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Procurement status couldn't be updated");
        }
        return ResponseEntity.ok(updatedStatusDto);
    }

    //get update-status objects for a procurment
    @GetMapping("/procurement/status/{id}")
    public ResponseEntity<?> getStatusUpdates(@PathVariable Long id){
        return ResponseEntity.ok(procurementService.getStatusUpdatesByProcurementId(id));
    }



    @GetMapping("/procurement/auditLog")
    public ResponseEntity<List<AuditLogDto>> getAuditLog(){
        return ResponseEntity.ok(auditLogService.getAuditLog());
    }


    @GetMapping("/procurement-status")
    public ResponseEntity<List<ProcurementStatusDto>> getProcurementStatus(){

        return ResponseEntity.ok(procurementService.getProcurementStatusList());
    }

    @GetMapping("/procurement-vendors")
    public ResponseEntity<List<VendorDto>> getVendors(){
        return ResponseEntity.ok(procurementService.getVendorsList());
    }

    @GetMapping("/procurement-users")
    public ResponseEntity<List<UserDto>> getProcurementUsers(){
        return ResponseEntity.ok(procurementService.getAssignedToUsersList());
    }

    @GetMapping("/procurement/requests")
    public ResponseEntity<List<RequestDto>> getRequestsForUpdateProcurement(){
        return ResponseEntity.ok(procurementService.getRequestsForUpdateProcurement());
    }
}
