package com.cht.procurementManagement.dto;

import jakarta.persistence.Column;

public class ProcurementStatusDto {
    private Long id;

    private String name;

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
}
