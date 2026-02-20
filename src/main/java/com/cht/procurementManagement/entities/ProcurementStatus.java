package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.ProcurementStatusDto;
import jakarta.persistence.*;

@Entity
public class ProcurementStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    //method to get dto
    public ProcurementStatusDto getProcurementStatusDto(){
        ProcurementStatusDto procurementStatusDto = new ProcurementStatusDto();
        procurementStatusDto.setId(id);
        procurementStatusDto.setName(name);
        return procurementStatusDto;
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
}
