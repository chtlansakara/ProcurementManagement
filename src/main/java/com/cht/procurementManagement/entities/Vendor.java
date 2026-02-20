package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.VendorDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date registeredDate;
    private String comments;

    //method to get dto
    public VendorDto getVendorDto(){
        VendorDto vendorDto = new VendorDto();
        vendorDto.setId(id);
        vendorDto.setName(name);
        vendorDto.setRegisteredDate(registeredDate);
        vendorDto.setComments(comments);
        return vendorDto;
    }

    //get-set methods

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

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
