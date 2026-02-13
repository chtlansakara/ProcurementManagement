package com.cht.procurementManagement.dto;

import java.util.Date;

public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String employeeId;
    private String nic;
    private Date birthdate;

    //getting as a String
    private String userrole;
    //getting related object ids
    private Long designationId;
    private Long subdivId;

//    get-set methods

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public Long getSubdivId() {
        return subdivId;
    }

    public void setSubdivId(Long subdivId) {
        this.subdivId = subdivId;
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

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }
}
