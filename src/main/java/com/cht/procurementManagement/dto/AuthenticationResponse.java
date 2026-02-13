package com.cht.procurementManagement.dto;

import com.cht.procurementManagement.enums.UserRole;

public class AuthenticationResponse {
    private String jwt;
    private Long id;
    private UserRole userRole;
    private String name;

    //get-set methods

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
