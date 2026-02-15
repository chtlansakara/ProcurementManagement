package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private String password;

    private String employeeId;
    private String nic;
    private Date birthdate;

    private String telephone;

    //user role from enum
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    //related objects
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_designation")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Designation designation;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name="user_subdiv")
    @OnDelete(action =OnDeleteAction.CASCADE)
    @JsonIgnore
    private Subdiv subdiv;
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name="user_admindiv")
    @OnDelete(action =OnDeleteAction.CASCADE)
    @JsonIgnore
    private Admindiv admindiv;

    //method to convert User to UserDto
    public UserDto getUserDto(){
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail(email);
//        userDto.setPassword(password);
        userDto.setEmployeeId(employeeId);
        userDto.setNic(nic);
        userDto.setBirthdate(birthdate);
        userDto.setTelephone(telephone);
        userDto.setUserRole(userRole);
        if(subdiv!= null){
            userDto.setSubdivId(subdiv.getId());
            userDto.setSubdivCode(subdiv.getCode());
            userDto.setSubdivName(subdiv.getName());
        }
        if(admindiv!= null){
            userDto.setAdmindivId(admindiv.getId());
            userDto.setAdmindivName(admindiv.getName());
            userDto.setDesignationId(designation.getId());
            userDto.setDesignationCode(designation.getCode());
        }

        return userDto;
    }

    //overridden methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //get-set methods

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admindiv getAdmindiv() {
        return admindiv;
    }

    public void setAdmindiv(Admindiv admindiv) {
        this.admindiv = admindiv;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Subdiv getSubdiv() {
        return subdiv;
    }

    public void setSubdiv(Subdiv subdiv) {
        this.subdiv = subdiv;
    }
}
