package com.cht.procurementManagement.controllers.admin;

import com.cht.procurementManagement.entities.Admindiv;
import com.cht.procurementManagement.services.admin.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdmindivController {
    private final AdminService adminService;

    public AdmindivController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    //create admin div
    @PostMapping("/admindivs")
    public ResponseEntity<Admindiv> createAdmindiv(@RequestBody Admindiv admindiv){
        Admindiv createdAdmindiv = adminService.createAdmindiv(admindiv);
        if(createdAdmindiv == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAdmindiv);
    }
    
    //get admin div list
    @GetMapping("/admindivs")
    public ResponseEntity<List<Admindiv>> getAdmindivs(){
        return ResponseEntity.ok(adminService.getAllAdmindivs());    }
    
    //get admin div by id
    @GetMapping("/admindivs/{id}")
    public ResponseEntity<Admindiv> getAdmindivById(@PathVariable Long id){
        return ResponseEntity.ok(adminService.getAdmindivById(id));
    }
    
    //update admin div
    @PutMapping("admindivs/{id}")
    public ResponseEntity<Admindiv> updateAdmindiv(@PathVariable Long id, @RequestBody Admindiv admindiv){
        Admindiv updatedAdmindiv = adminService.updateAdmindiv(id, admindiv);
        if(updatedAdmindiv == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAdmindiv);
    }
    
    //delete admin div
    @DeleteMapping("admindivs/{id}")
    public ResponseEntity<Void> deleteAdmindiv(@PathVariable Long id){
        adminService.deleteAdmindiv(id);
        return ResponseEntity.ok(null);
    }
}
