package com.cht.procurementManagement.controllers.admin;

import com.cht.procurementManagement.dto.SignupRequest;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.Designation;
import com.cht.procurementManagement.services.admin.AdminService;
import com.cht.procurementManagement.services.auth.AuthService;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {
    //injecting service class
    private final AdminService adminService;
    private final AuthService authService;
    public AdminController(AdminService adminService, AuthService authService) {
        this.adminService = adminService;
        this.authService = authService;
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




}
