package com.cht.procurementManagement.controllers.admin;

import com.cht.procurementManagement.dto.ProcurementSourceDto;
import com.cht.procurementManagement.dto.ProcurementStatusDto;
import com.cht.procurementManagement.services.admin.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@CrossOrigin("*")
public class ProcurementSourcesController {
    private final AdminService adminService;

    public ProcurementSourcesController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @PostMapping("/sources")
    public ResponseEntity<?> createProcurementSource(@RequestBody ProcurementSourceDto dto){
        ProcurementSourceDto createdDto = adminService.createSource(dto);
        if(createdDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }


    @GetMapping("/sources")
    public ResponseEntity<?> getProcurementSources(){
        try{
            return ResponseEntity.ok(adminService.getAllSources());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/sources/{id}")
    public ResponseEntity<?> getProcurementSourceById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(adminService.getSourceById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/sources/{id}")
    public ResponseEntity<?> updateProcurementSourceById(@PathVariable Long id, @RequestBody ProcurementSourceDto dto){
        ProcurementSourceDto updatedDto = adminService.updateSource(id, dto);
        if(updatedDto== null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source couldn't be updated");
        }
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/sources/{id}")
    public ResponseEntity<?> deleteSource(@PathVariable Long id){
        try {
            adminService.deleteSourceById(id);
            return ResponseEntity.ok(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    


}
