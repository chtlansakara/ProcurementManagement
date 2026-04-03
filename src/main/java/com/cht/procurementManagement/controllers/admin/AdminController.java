package com.cht.procurementManagement.controllers.admin;

import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.enums.AuditEntityType;
import com.cht.procurementManagement.services.AuditLog.AuditLogService;
import com.cht.procurementManagement.services.admin.AdminService;
import com.cht.procurementManagement.services.auth.AuthService;
import com.cht.procurementManagement.services.requests.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {
    //injecting service class
    private final AdminService adminService;
    private final AuthService authService;
    private final AuditLogService auditLogService;
    private final RequestService requestService;
    public AdminController(AdminService adminService, AuthService authService, AuditLogService auditLogService, RequestService requestService) {
        this.adminService = adminService;
        this.authService = authService;
        this.auditLogService = auditLogService;
        this.requestService = requestService;
    }


    //get users list
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }

    //create user
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto){

        UserDto createdUserDto = adminService.createUser(userDto);
        //check if created
        if(createdUserDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    //get user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    //update user
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto){
        UserDto updatedUserDto = adminService.updateUser(id, userDto);
        if(updatedUserDto == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUserDto);
    }

    //delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        adminService.deleteUser(id);
        return ResponseEntity.ok(null);
    }


    //delete audit logs for a procurement
    @DeleteMapping("/procurement/auditLog/{id}")
    public ResponseEntity<Void> deleteAuditLog(@PathVariable Long id){
        auditLogService.deleteAuditlogs(AuditEntityType.PROCUREMENT, id);
        return ResponseEntity.ok(null);
    }

    //delete requests (force delete in testing)
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Void> deleteRequestByForce(@PathVariable Long id){
        requestService.deleteRequestById(id);
        return ResponseEntity.ok(null);
    }

}
