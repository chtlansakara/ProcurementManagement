package com.cht.procurementManagement.controllers.admin;

import com.cht.procurementManagement.dto.ProcurementStatusDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.services.admin.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@CrossOrigin("*")
public class StatusController {
    private final AdminService adminService;

    public StatusController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/status")
    public ResponseEntity<?> createProcurementStatus(@RequestBody ProcurementStatusDto procurementStatusDto){
        ProcurementStatusDto createdDto = adminService.createProcurementStatus(procurementStatusDto);
        if(createdDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getProcurementStatus(){
        try{
            return ResponseEntity.ok(adminService.getProcurementStatus());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> getProcurementStatusById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(adminService.getProcurementStatusById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateProcurementStatusById(@PathVariable Long id, @RequestBody ProcurementStatusDto procurementStatusDto){
        ProcurementStatusDto updatedDto = adminService.updateProcurementStatus(id, procurementStatusDto);
        if(updatedDto== null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status couldn't be updated");
        }
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/status/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable Long id){
        try {
            adminService.deleteProcurementStatus(id);
            return ResponseEntity.ok(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
