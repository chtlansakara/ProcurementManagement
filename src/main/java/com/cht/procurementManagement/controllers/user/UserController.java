package com.cht.procurementManagement.controllers.user;

import com.cht.procurementManagement.dto.PasswordDto;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.Designation;
import com.cht.procurementManagement.services.admin.AdminService;
import com.cht.procurementManagement.services.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
    private final AuthService authService;
    private final AdminService adminService;


    public UserController(AuthService authService,
                          AdminService adminService) {
        this.authService = authService;
        this.adminService = adminService;
    }


    //get logged user details
    @GetMapping("/details")
    public ResponseEntity<UserDto> getLoggedUserDetails(){
        return ResponseEntity.ok(authService.getLoggedUserDto());
    }

    //get designations list
    @GetMapping("/designations")
    public ResponseEntity<List<Designation>> getDesignations(){
        return ResponseEntity.ok(adminService.getAllDesignations());
    }


    //update user password
    @PutMapping("/password")
    public ResponseEntity<UserDto> updateLoggedUserPassword(@RequestBody PasswordDto changePassword){
        UserDto updatedUserDto = authService.updateUserPassword(changePassword.getCurrentPassword(), changePassword.getPassword());
        if(updatedUserDto == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUserDto);
    }

    //uses admin service
    @PutMapping("/details")
    public ResponseEntity<UserDto> updateLoggedUserDetails( @RequestBody UserDto userDto){
        UserDto updatedUserDto = authService.updateUserDetails(userDto);
        if(updatedUserDto == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUserDto);
    }



}
