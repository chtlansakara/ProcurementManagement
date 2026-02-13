package com.cht.procurementManagement.dto;

import jakarta.persistence.Column;

public class SubdivDto {
    private Long id;
    private String email;
    private String name;
    private String code;
    private String telephone;
    private String address;

    //admin div info
    private Long admindivId;
    private String admindivName;
    private String admindivCode;

    //get-set methods

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAdmindivId() {
        return admindivId;
    }

    public void setAdmindivId(Long admindivId) {
        this.admindivId = admindivId;
    }

    public String getAdmindivName() {
        return admindivName;
    }

    public void setAdmindivName(String admindivName) {
        this.admindivName = admindivName;
    }

    public String getAdmindivCode() {
        return admindivCode;
    }

    public void setAdmindivCode(String admindivCode) {
        this.admindivCode = admindivCode;
    }
}
