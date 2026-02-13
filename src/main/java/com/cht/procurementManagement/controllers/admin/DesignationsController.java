package com.cht.procurementManagement.controllers.admin;

import com.cht.procurementManagement.entities.Designation;
import com.cht.procurementManagement.services.admin.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/admin")
@CrossOrigin("*")
public class DesignationsController {
    //injecting
    private final AdminService adminService;

    public DesignationsController(AdminService adminService) {
        this.adminService = adminService;
    }

    //create designation
    @PostMapping("/designations")
    public ResponseEntity<Designation> createDesignation(@RequestBody Designation designation){
        Designation createdDesignation = adminService.createDesignation(designation);
        if(createdDesignation == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDesignation);
    }

    //get designations list
    @GetMapping("/designations")
    public ResponseEntity<List<Designation>> getDesignations(){
        return ResponseEntity.ok(adminService.getAllDesignations());
    }

    //get designation by id
    @GetMapping("/designations/{id}")
    public ResponseEntity<Designation> getDesignationById(@PathVariable Long id){
        return ResponseEntity.ok(adminService.getDesignationById(id));
    }

    //update designation
    @PutMapping("designations/{id}")
    public ResponseEntity<Designation> updateDesignation(@PathVariable Long id, @RequestBody Designation designation){
        Designation updatedDesignation = adminService.updateDesignation(id, designation);
        if(updatedDesignation == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedDesignation);
    }

    //delete designation
    @DeleteMapping("designations/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable Long id){
        adminService.deleteDesignation(id);
        return ResponseEntity.ok(null);
    }
}
