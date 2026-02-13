package com.cht.procurementManagement.controllers.admin;

import com.cht.procurementManagement.dto.SubdivDto;
import com.cht.procurementManagement.services.admin.AdminService;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class SubdivController {
    private final AdminService adminService;
    public SubdivController(AdminService adminService) {
        this.adminService = adminService;
    }

    //create subdiv
    @PostMapping("/subdivs")
    public ResponseEntity<SubdivDto> createSubdiv(@RequestBody SubdivDto subdivDto){
        SubdivDto createdSubdivDto = adminService.createSubdiv(subdivDto);
        if(createdSubdivDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubdivDto);
    }

    //get subdiv list
    @GetMapping("/subdivs")
    public ResponseEntity<List<SubdivDto>> getSubdivs(){
        return ResponseEntity.ok(adminService.getSubdivs());
    }

    //get subdiv by id
    @GetMapping("/subdivs/{id}")
    public ResponseEntity<SubdivDto> getSubdivById(@PathVariable Long id){
        return ResponseEntity.ok(adminService.getSubdivById(id));
    }

    //get subdiv list by admin id
    @GetMapping("/subdivs/admindiv/{id}")
    public ResponseEntity<List<SubdivDto>> getSubdivsByAdmindivId(@PathVariable  Long id){
        return ResponseEntity.ok(adminService.getSubdivsByAdmindivId(id));
    }

    //update subdiv
    @PutMapping("/subdivs/{id}")
    public ResponseEntity<SubdivDto> updateSubdiv(@PathVariable Long id, @RequestBody SubdivDto subdivDto){
        SubdivDto updatedSubdivDto = adminService.updateSubdiv(id, subdivDto);
        if(updatedSubdivDto == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedSubdivDto);
    }

    //delete subdiv
    @DeleteMapping("/subdivs/{id}")
    public ResponseEntity<Void> deleteSubdiv(@PathVariable Long id){
        adminService.deleteSubdiv(id);
        return ResponseEntity.ok(null);
    }

}
