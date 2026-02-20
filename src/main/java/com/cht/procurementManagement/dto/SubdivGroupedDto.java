package com.cht.procurementManagement.dto;

import java.util.List;

public class SubdivGroupedDto {
    private Long admindivId;
    private String admindivName;
    private List<SubdivDto> subdivDtoList;

    public SubdivGroupedDto(Long admindivId, String admindivName, List<SubdivDto> subdivDtoList) {
        this.admindivId = admindivId;
        this.admindivName = admindivName;
        this.subdivDtoList = subdivDtoList;
    }

    //get-set methods

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

    public List<SubdivDto> getSubdivDtoList() {
        return subdivDtoList;
    }

    public void setSubdivDtoList(List<SubdivDto> subdivDtoList) {
        this.subdivDtoList = subdivDtoList;
    }
}
