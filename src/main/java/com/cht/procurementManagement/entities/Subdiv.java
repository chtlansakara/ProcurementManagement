package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.SubdivDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Subdiv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    private String telephone;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admindiv_id", nullable = false)
    @JsonIgnore
    private Admindiv admindiv;

    //method to get dto
    public SubdivDto getSubdivDto(){
        SubdivDto subdivDto = new SubdivDto();
        subdivDto.setId(id);
        subdivDto.setEmail(email);
        subdivDto.setName(name);
        subdivDto.setCode(code);
        subdivDto.setTelephone(telephone);
        subdivDto.setAddress(address);
        if(admindiv!= null) {
            subdivDto.setAdmindivId(admindiv.getId());
            subdivDto.setAdmindivName(admindiv.getName());
            subdivDto.setAdmindivCode(admindiv.getCode());
        }
        return subdivDto;
    }

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

    public Admindiv getAdmindiv() {
        return admindiv;
    }

    public void setAdmindiv(Admindiv admindiv) {
        this.admindiv = admindiv;
    }
}
