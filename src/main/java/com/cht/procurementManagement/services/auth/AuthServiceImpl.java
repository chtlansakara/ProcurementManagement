package com.cht.procurementManagement.services.auth;

import com.cht.procurementManagement.dto.SignupRequest;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.Admindiv;
import com.cht.procurementManagement.entities.Designation;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.DesignationRepository;
import com.cht.procurementManagement.repositories.SubdivRepository;
import com.cht.procurementManagement.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AuthServiceImpl implements AuthService{
    //injecting User Repository
    private final UserRepository userRepository;
    private final DesignationRepository designationRepository;
    private final SubdivRepository subdivRepository;

    public AuthServiceImpl(UserRepository userRepository,
                           DesignationRepository designationRepository,
                           SubdivRepository subdivRepository) {
        this.userRepository = userRepository;
        this.designationRepository = designationRepository;
        this.subdivRepository = subdivRepository;
    }

    //method- to create an automatic - admin account if not exists already
    @PostConstruct
    public void createAdminAccount(){
        //find admin account if already exists
        List<User> admins = userRepository.findByUserRole(UserRole.ADMIN);
        //otherwise create a new account
        if(admins.isEmpty()){
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("ADMIN");
            user.setPassword(new BCryptPasswordEncoder().encode("Admin7777"));
            user.setUserRole(UserRole.ADMIN);
            //save to db
            userRepository.save(user);
            System.out.println("Admin account created successfully!");
        }else{
            System.out.println("Admin account already exists");
        }
    }

    //sign up a user
    @Override
    public UserDto signupUser(SignupRequest signupRequest) {
        //finding objects
        Optional<Designation> optionalDesignation = designationRepository.findById(signupRequest.getDesignationId());
        Optional<Subdiv> optionalSubdiv = subdivRepository.findById(signupRequest.getSubdivId());
        //if both present, create new User
        if(optionalSubdiv.isPresent() && optionalDesignation.isPresent()) {
            User user = new User();
            user.setEmail(signupRequest.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
            user.setUserRole(mapStringToUserRole(signupRequest.getUserrole()));
            user.setName(signupRequest.getName());
            user.setEmployeeId(signupRequest.getEmployeeId());
            user.setNic(signupRequest.getNic());
            user.setBirthdate(signupRequest.getBirthdate());
            user.setDesignation(optionalDesignation.get());
            user.setSubdiv(optionalSubdiv.get());

            //saving to database
            User createdUser = userRepository.save(user);
            return createdUser.getUserDto();
        }
        //otherwise return null
        return null;
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }

    @Override
    public UserDto getLoggedUserDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        //get user from database
        User user = userRepository.findFirstByEmail(email)
                .orElseThrow( () -> new RuntimeException("Logged user not found."));
        return user.getUserDto();
    }

    @Override
    public UserDto updateUserPassword(String currentPassword, String password) {
        User existingUser = getLoggedUser();
        String existingPassword = existingUser.getPassword();
        //check the current password
        if(!new BCryptPasswordEncoder().matches(currentPassword, existingPassword)){
            throw new RuntimeException("Current password is incorrect");
        }

        existingUser.setPassword(new BCryptPasswordEncoder().encode(password));
        //saving to database & return
        return userRepository.save(existingUser).getUserDto();
    }

    @Override
    public UserDto updateUserDetails(UserDto userDto) {
        User existingUser = getLoggedUser();

        //finding its designation
        Designation existingDesignation = designationRepository.findById(userDto.getDesignationId())
                .orElseThrow(() -> new EntityNotFoundException("Designation not found!"));
        existingUser.setName(userDto.getName());
        existingUser.setTelephone(userDto.getTelephone());
        existingUser.setBirthdate(userDto.getBirthdate());
        //setting related objects
        existingUser.setDesignation(existingDesignation);
        //saving to database & return
        return userRepository.save(existingUser).getUserDto();
    }

    private User getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        //get user from database
        User user = userRepository.findFirstByEmail(email)
                .orElseThrow( () -> new RuntimeException("Logged user not found."));
        return user;
    }

    private UserRole mapStringToUserRole(String userRole){
        return switch (userRole){
            case "ADMIN" -> UserRole.ADMIN;
            case "ADMINDIVUSER" -> UserRole.ADMINDIVUSER;
            case "SUBDIVUSER" -> UserRole.SUBDIVUSER;
            default -> UserRole.SUPPLIESUSER;
        };
    }


}
