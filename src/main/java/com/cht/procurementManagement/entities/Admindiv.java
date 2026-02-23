package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.AdmindivDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Admindiv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    private String telephone;
    private String address;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "designation_id")
    @JsonIgnore
    private Designation responsibleDesignation;


    //conversion method to dto
    public AdmindivDto getAdmindivDto(){
        AdmindivDto admindivDto = new AdmindivDto();
        admindivDto.setId(id);
        admindivDto.setEmail(email);
        admindivDto.setName(name);
        admindivDto.setCode(code);
        admindivDto.setTelephone(telephone);
        admindivDto.setAddress(address);
        //setting object
        if(responsibleDesignation!= null) {
            admindivDto.setResponsibleDesignationId(responsibleDesignation.getId());
            admindivDto.setResponsibleDesignationTitle(responsibleDesignation.getTitle());
            admindivDto.setResponsibleDesignationCode(responsibleDesignation.getCode());
        }
        return admindivDto;
    }



    //get-set methods
    public Long getId() {
        return id;
    }

    public Designation getResponsibleDesignation() {
        return responsibleDesignation;
    }

    public void setResponsibleDesignation(Designation responsibleDesignation) {
        this.responsibleDesignation = responsibleDesignation;
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
}
