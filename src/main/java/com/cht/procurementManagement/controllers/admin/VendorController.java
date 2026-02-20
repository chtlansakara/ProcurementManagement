package com.cht.procurementManagement.controllers.admin;

import com.cht.procurementManagement.dto.VendorDto;
import com.cht.procurementManagement.services.admin.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@CrossOrigin("*")
public class VendorController {
    private final AdminService adminService;

    public VendorController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @PostMapping("/vendors")
    public ResponseEntity<?> createVendor(@RequestBody VendorDto vendorDto){
        VendorDto createdDto = adminService.createVendor(vendorDto);
        if(createdDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vendor couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }
    @GetMapping("/vendors")
    public ResponseEntity<?> getVendor(){
        try{
            return ResponseEntity.ok(adminService.getVendors());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/vendors/{id}")
    public ResponseEntity<?> getVendorById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(adminService.getVendorById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/vendors/{id}")
    public ResponseEntity<?> updateVendorById(@PathVariable Long id, @RequestBody VendorDto vendorDto){
        VendorDto updatedDto = adminService.updateVendor(id, vendorDto);
        if(updatedDto== null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vendor couldn't be updated");
        }
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/vendors/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id){
        try {
            adminService.deleteVendor(id);
            return ResponseEntity.ok(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
