package com.cht.procurementManagement.dto;

import jakarta.persistence.Column;

public class AdmindivDto {
    private Long id;
    private String email;

    private String name;

    private String code;
    private String telephone;
    private String address;

    //representing object
    private Long responsibleDesignationId;
    private String responsibleDesignationTitle;
    private String responsibleDesignationCode;

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

    public Long getResponsibleDesignationId() {
        return responsibleDesignationId;
    }

    public void setResponsibleDesignationId(Long responsibleDesignationId) {
        this.responsibleDesignationId = responsibleDesignationId;
    }

    public String getResponsibleDesignationTitle() {
        return responsibleDesignationTitle;
    }

    public void setResponsibleDesignationTitle(String responsibleDesignationTitle) {
        this.responsibleDesignationTitle = responsibleDesignationTitle;
    }

    public String getResponsibleDesignationCode() {
        return responsibleDesignationCode;
    }

    public void setResponsibleDesignationCode(String responsibleDesignationCode) {
        this.responsibleDesignationCode = responsibleDesignationCode;
    }
}
