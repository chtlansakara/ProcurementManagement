package com.cht.procurementManagement.services.admin;

import com.cht.procurementManagement.dto.SubdivDto;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.Admindiv;
import com.cht.procurementManagement.entities.Designation;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.AdmindivRepository;
import com.cht.procurementManagement.repositories.DesignationRepository;
import com.cht.procurementManagement.repositories.SubdivRepository;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.admin.AdminService;
import jakarta.persistence.EntityExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.expression.ExpressionException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    //repository injections

    private final AdmindivRepository admindivRepository;
    private final DesignationRepository designationRepository;
    private final SubdivRepository subdivRepository;
    private final UserRepository userRepository;

    public AdminServiceImpl(
            AdmindivRepository admindivRepository,
            DesignationRepository designationRepository,
            SubdivRepository subdivRepository,
            UserRepository userRepository
    ) {
        this.admindivRepository = admindivRepository;
        this.designationRepository = designationRepository;
        this.subdivRepository = subdivRepository;
        this.userRepository = userRepository;
    }

    //Users -----------------------------------------------------------------------------



    @Override
    public UserDto createUser(UserDto userDto) {
        //check for unique constraints
//        if(userRepository.existsByEmail(userDto.getEmail())){
//            throw new RuntimeException("Email already exists");
//        }

        if(userRepository.findFirstByEmail(userDto.getEmail()).isPresent()){
            throw new EntityExistsException("Email already exists");
        }


        //finding its sub div
        Optional<Subdiv> optionalSubdiv = subdivRepository.findById(userDto.getSubdivId());
        //finding its admin div
        Optional<Admindiv> optionalAdmindiv = admindivRepository.findById(userDto.getAdmindivId());
        //finding its designation
        Optional<Designation> optionalDesignation = designationRepository.findById(userDto.getDesignationId());

        if(optionalSubdiv.isPresent() && optionalAdmindiv.isPresent() && optionalDesignation.isPresent()){
            User user = new User();
            user.setEmail(userDto.getEmail());
            //map user role string to enum
            user.setUserRole(mapStringToUserRole(String.valueOf(userDto.getUserRole())));
            user.setName(userDto.getName());
            user.setEmployeeId(userDto.getEmployeeId());
            user.setNic(userDto.getNic());
            //setting password as nic automatically
            user.setPassword( new BCryptPasswordEncoder().encode(userDto.getNic()));
            user.setTelephone(userDto.getTelephone());
            user.setBirthdate(userDto.getBirthdate());
            //setting objects
            user.setDesignation(optionalDesignation.get());
            user.setAdmindiv(optionalAdmindiv.get());
            user.setSubdiv(optionalSubdiv.get());
            //saving to database & return
            User createdUser = userRepository.save(user);
            return createdUser.getUserDto();
        }
        return null;
    }

    //method to map user role enum with its string values
    private UserRole mapStringToUserRole(String userRole){
        return switch (userRole){
            case "ADMIN" -> UserRole.ADMIN;
            case "ADMINDIVUSER" -> UserRole.ADMINDIVUSER;
            case "SUBDIVUSER" -> UserRole.SUBDIVUSER;
            default -> UserRole.SUPPLIESUSER;
        };
    }


    //get all users - need to filter out ADMIN as he doesn't have subdiv
    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user-> user.getUserRole() != UserRole.ADMIN)
                .sorted(Comparator.comparing(User::getEmail))
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(User::getUserDto)
                .orElseThrow( () -> new ExpressionException("User with id "+id+ " is not found!"));
    }


    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        //find the User object
        Optional<User> optionalUser = userRepository.findById(id);

        //finding its admin div
        Optional<Admindiv> optionalAdmindiv = admindivRepository.findById(userDto.getAdmindivId());
        //finding its sub div
        Optional<Subdiv> optionalSubdiv = subdivRepository.findById(userDto.getSubdivId());
        //finding its designation
        Optional<Designation> optionalDesignation = designationRepository.findById(userDto.getDesignationId());

        if (optionalUser.isPresent() && optionalSubdiv.isPresent() && optionalDesignation.isPresent() && optionalAdmindiv.isPresent()) {
            User existingUser = optionalUser.get();
            //update with new details
            existingUser.setEmail(userDto.getEmail());
            //change password only if there is a value provided
            if(userDto.getPassword()!= null){
                existingUser.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
            }
            //map user role string to enum
            existingUser.setUserRole(mapStringToUserRole(String.valueOf(userDto.getUserRole())));
            existingUser.setName(userDto.getName());
            existingUser.setEmployeeId(userDto.getEmployeeId());
            existingUser.setNic(userDto.getNic());
            existingUser.setTelephone(userDto.getTelephone());
            existingUser.setBirthdate(userDto.getBirthdate());
            //setting related objects
            existingUser.setDesignation(optionalDesignation.get());
            existingUser.setSubdiv(optionalSubdiv.get());
            existingUser.setAdmindiv(optionalAdmindiv.get());
            //saving to database & return
            return userRepository.save(existingUser).getUserDto();

        }
        return null;
    }


    @Override
    public void deleteUser(Long id) {
        Optional<User> optionalUser= userRepository.findById(id);
        if(!optionalUser.isPresent()) {
            throw new ExpressionException("User with id "+id+ " is not found!");
        }
        userRepository.deleteById(id);
    }

    //Sub div -----------------------------------------------------------------------------

    @Override
    public SubdivDto createSubdiv(SubdivDto subdivDto) {
        //check for unique constraints
        if(subdivRepository.existsByCode(subdivDto.getCode())){
            throw new RuntimeException("Code already exists");
        }

        //finding its admin div
        Optional<Admindiv> optionalAdmindiv = admindivRepository.findById(subdivDto.getAdmindivId());
        if(optionalAdmindiv.isPresent()){
            Subdiv subdiv = new Subdiv();
            subdiv.setId(subdivDto.getId());
            subdiv.setEmail(subdivDto.getEmail());
            subdiv.setName(subdivDto.getName());
            subdiv.setCode(subdivDto.getCode());
            subdiv.setTelephone(subdivDto.getTelephone());
            subdiv.setAddress(subdivDto.getAddress());
            subdiv.setAdmindiv(optionalAdmindiv.get());
            //save amd return
            return subdivRepository.save(subdiv).getSubdivDto();
        }
        return null;
    }

    @Override
    public List<SubdivDto> getSubdivs() {
        return subdivRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Subdiv::getCode))
                .map(Subdiv::getSubdivDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubdivDto getSubdivById(Long id) {
        Optional<Subdiv> optionalSubdiv = subdivRepository.findById(id);
        return optionalSubdiv.map(Subdiv::getSubdivDto)
                .orElseThrow( () -> new ExpressionException("Sub division with id "+id+ " is not found!"));
    }

    @Override
    public List<SubdivDto> getSubdivsByAdmindivId(Long id) {
        return subdivRepository.findByAdmindivId(id)
                .stream()
                .sorted(Comparator.comparing(Subdiv::getCode))
                .map(Subdiv::getSubdivDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubdivDto updateSubdiv(Long id, SubdivDto subdivDto) {
        //check for sub div
        Optional<Subdiv> optionalSubdiv = subdivRepository.findById(id);
        //finding the admin div
        Optional<Admindiv> optionalAdmindiv = admindivRepository.findById(subdivDto.getAdmindivId());

        //if both present -
        if(optionalSubdiv.isPresent() && optionalAdmindiv.isPresent()){

            //get object from optional
            Subdiv existingSubdiv = optionalSubdiv.get();

            //check for unique constraints - if not the same
            if(!(existingSubdiv.getCode().equals(subdivDto.getCode())) && subdivRepository.existsByCode(subdivDto.getCode())){
                throw new RuntimeException("Code already exists");
            }


            existingSubdiv.setEmail(subdivDto.getEmail());
            existingSubdiv.setName(subdivDto.getName());
            existingSubdiv.setCode(subdivDto.getCode());
            existingSubdiv.setTelephone(subdivDto.getTelephone());
            existingSubdiv.setAddress(subdivDto.getAddress());
            //setting the admin div object
            existingSubdiv.setAdmindiv(optionalAdmindiv.get());
            //save & return as dto
            return  subdivRepository.save(existingSubdiv).getSubdivDto();
        }
        return null;
    }

    @Override
    public void deleteSubdiv(Long id) {
        Optional<Subdiv> optionalSubdiv= subdivRepository.findById(id);
        if(!optionalSubdiv.isPresent()) {
            throw new ExpressionException("Sub division with id "+id+ " is not found!");
        }
        subdivRepository.deleteById(id);
    }


    //Admin div -----------------------------------------------------------------------------



    @Override
    public Admindiv createAdmindiv(Admindiv admindiv) {
        //check for unique constraints
        if(admindivRepository.existsByCode(admindiv.getCode())){
            throw new RuntimeException("Code already exists");
        }
        if(admindivRepository.existsByName(admindiv.getName())){
            throw new RuntimeException("Name already exists");
        }
        return admindivRepository.save(admindiv);
    }

    @Override
    public List<Admindiv> getAllAdmindivs() {
        return admindivRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Admindiv::getCode))
                .collect(Collectors.toList());
    }

    @Override
    public Admindiv getAdmindivById(Long id) {
        //returns null if not found
        Optional<Admindiv> optionalAdmindiv = admindivRepository.findById(id);
        return optionalAdmindiv
                .orElseThrow( () -> new ExpressionException("Admin division with id "+id+ " is not found!") );
    }

    @Override
    public Admindiv updateAdmindiv(Long id, Admindiv admindiv) {
        Optional<Admindiv> optionalAdmindiv = admindivRepository.findById(id);

        if(optionalAdmindiv.isPresent()){
            Admindiv existingAdmindiv = optionalAdmindiv.get();

            //check for unique constraints - if not the same
            if(!(existingAdmindiv.getCode().equals(admindiv.getCode())) && admindivRepository.existsByCode(admindiv.getCode())){
                throw new RuntimeException("Code already exists");
            }
            if(!(existingAdmindiv.getName().equals(admindiv.getName()))&& admindivRepository.existsByName(admindiv.getName())){
                throw new RuntimeException("Name already exists");
            }
            existingAdmindiv.setEmail(admindiv.getEmail());
            existingAdmindiv.setName(admindiv.getName());
            existingAdmindiv.setCode(admindiv.getCode());
            existingAdmindiv.setTelephone(admindiv.getTelephone());
            existingAdmindiv.setAddress(admindiv.getAddress());
            return  admindivRepository.save(existingAdmindiv);
        }
        return null;
    }

    @Override
    public void deleteAdmindiv(Long id) {
        Optional<Admindiv> optionalAdmindiv= admindivRepository.findById(id);
        if(!optionalAdmindiv.isPresent()) {
            throw new ExpressionException("Admin division with id "+id+ " is not found!");
        }
        admindivRepository.deleteById(id);
    }


    //Designations -----------------------------------------------------------------------------

    //create designation
    @Override
    public Designation createDesignation(Designation newDesignation) {
        //check for unique constraints
        if(designationRepository.existsByCode(newDesignation.getCode())){
            throw new RuntimeException("Code already exists");
        }
        //check for compound unique constraints
        if(designationRepository.existsByTitleAndGrade(newDesignation.getTitle(), newDesignation.getGrade())){
            throw new RuntimeException("Title & grade already exists");
        }
        try{
            return designationRepository.save(newDesignation);
        }catch (DataIntegrityViolationException e){
            throw new RuntimeException("Duplicate entry for title + grade");
        }

    }
    //get designation list
    @Override
    public List<Designation> getAllDesignations() {
        //return sorted by title
        return this.designationRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Designation::getTitle))
                .collect(Collectors.toList());
    }
    //get designation by id
    @Override
    public Designation getDesignationById(Long id) {
        //returns null if not found
        Optional<Designation> optionalDesignation = designationRepository.findById(id);
        return optionalDesignation.orElseThrow( () -> new ExpressionException("Designation with id "+id+ " is not found!") );
    }

    //update designation
    @Override
    public Designation updateDesignation(Long id, Designation designation) {
        Optional<Designation> optionalDesignation = designationRepository.findById(id);
        if(optionalDesignation.isPresent()){
            Designation existingDesignation = optionalDesignation.get();
            //check for unique constraints - if not the same
            if(!(existingDesignation.getCode().equals(designation.getCode())) && designationRepository.existsByCode(designation.getCode())){
                throw new RuntimeException("Code already exists");
            }
            if(!(existingDesignation.getTitle().equals(designation.getTitle()) && existingDesignation.getGrade().equals(designation.getGrade())) && designationRepository.existsByTitleAndGrade(designation.getTitle(), designation.getGrade())){
                throw new RuntimeException("Title & grade already exists");
            }
            existingDesignation.setTitle(designation.getTitle());
            existingDesignation.setGrade(designation.getGrade());
            existingDesignation.setCode(designation.getCode());
            return  designationRepository.save(existingDesignation);
        }
        return null;
    }

    //delete designation
    @Override
    public void deleteDesignation(Long id) {
        Optional<Designation> optionalDesignation = designationRepository.findById(id);
        if(!optionalDesignation.isPresent()) {
            throw new ExpressionException("Designation with id "+id+ " is not found!");
        }
        designationRepository.deleteById(id);
    }

//-----------------------------------------------------------------------------------------------

}
