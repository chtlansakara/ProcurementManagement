package com.cht.procurementManagement.controllers.auth;

import com.cht.procurementManagement.dto.AuthenticationRequest;
import com.cht.procurementManagement.dto.AuthenticationResponse;
import com.cht.procurementManagement.dto.SignupRequest;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.auth.AuthService;
import com.cht.procurementManagement.services.jwt.UserService;
import com.cht.procurementManagement.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;
    //for login
    private final UserRepository userRepository;
    private final UserService userService;
    //to check token
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService,
                          UserRepository userRepository,
                          UserService userService,
                          JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    //sign up user API method
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
        //check if user email exists
        if(authService.hasUserWithEmail(signupRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Email already exists!");
        }
        UserDto createdUserDto = authService.signupUser(signupRequest);
        //check if created
        if(createdUserDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    //login in user API method
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest){
        //authenticate with email and password
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()
            ));
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Incorrect password or username!");
        }
        //for correct credentials
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        //load user from db
        Optional<User> optionalUser = userRepository.findFirstByEmail(authenticationRequest.getEmail());
        //create token
        final String jwtToken = jwtUtil.generateToken(userDetails);
        //create response
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        //if user exists, set response
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwtToken);
            authenticationResponse.setId(optionalUser.get().getId());
            authenticationResponse.setName(optionalUser.get().getName());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }

}
