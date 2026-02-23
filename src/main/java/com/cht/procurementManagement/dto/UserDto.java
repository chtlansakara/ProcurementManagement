package com.cht.procurementManagement.dto;

import com.cht.procurementManagement.enums.UserRole;

import java.util.Date;

public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String employeeId;
    private String nic;
    private String telephone;
    private Date birthdate;

    //user role from enum
    private UserRole userRole;
    //instead of objects
    //for subdiv
    private Long subdivId;
    private String subdivName;
    private String subdivCode;
    //for admindiv
    private Long admindivId;
    private String admindivName;
    //for designation
    private Long designationId;
    private String designationCode;



    //get-set methods


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getSubdivId() {
        return subdivId;
    }

    public void setSubdivId(Long subdivId) {
        this.subdivId = subdivId;
    }

    public String getSubdivName() {
        return subdivName;
    }

    public void setSubdivName(String subdivName) {
        this.subdivName = subdivName;
    }

    public String getSubdivCode() {
        return subdivCode;
    }

    public void setSubdivCode(String subdivCode) {
        this.subdivCode = subdivCode;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public String getDesignationCode() {
        return designationCode;
    }

    public void setDesignationCode(String designationCode) {
        this.designationCode = designationCode;
    }
}
