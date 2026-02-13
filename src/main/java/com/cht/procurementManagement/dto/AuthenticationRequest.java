package com.cht.procurementManagement.dto;

public class AuthenticationRequest {
    private String email;
    private String password;

    //get-set methods

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
