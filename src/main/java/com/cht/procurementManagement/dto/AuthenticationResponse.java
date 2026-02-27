package com.cht.procurementManagement.dto;

import com.cht.procurementManagement.enums.UserRole;

public class AuthenticationResponse {
    private String jwt;
    private Long id;
    private UserRole userRole;
    private String email;
    private String name;
    private String subdivName;
    private String subdivCode;
    private String admindivName;
    private String admindivCode;

    //get-set methods

    public String getAdmindivCode() {
        return admindivCode;
    }

    public void setAdmindivCode(String admindivCode) {
        this.admindivCode = admindivCode;
    }

    public String getSubdivCode() {
        return subdivCode;
    }

    public void setSubdivCode(String subdivCode) {
        this.subdivCode = subdivCode;
    }

    public String getSubdivName() {
        return subdivName;
    }

    public void setSubdivName(String subdivName) {
        this.subdivName = subdivName;
    }

    public String getAdmindivName() {
        return admindivName;
    }

    public void setAdmindivName(String admindivName) {
        this.admindivName = admindivName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
