package com.cht.procurementManagement.entities;

import com.cht.procurementManagement.dto.ProcurementSourceDto;
import jakarta.persistence.*;

@Entity
public class ProcurementSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    private String description;



    //get dto method
    public ProcurementSourceDto getdto(){
        ProcurementSourceDto dto = new ProcurementSourceDto();
        dto.setId(id);
        dto.setDescription(description);
        dto.setName(name);
        return dto;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
