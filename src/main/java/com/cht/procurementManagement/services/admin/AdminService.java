package com.cht.procurementManagement.services.admin;

import com.cht.procurementManagement.dto.ProcurementStatusDto;
import com.cht.procurementManagement.dto.SubdivDto;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.dto.VendorDto;
import com.cht.procurementManagement.entities.Admindiv;
import com.cht.procurementManagement.entities.Designation;

import java.util.List;

public interface AdminService {
//vendor ----------------------------
    VendorDto createVendor(VendorDto vendorDto);
    List<VendorDto> getVendors();
    VendorDto getVendorById(Long id);
    VendorDto updateVendor(Long id, VendorDto vendorDto);
    void deleteVendor(Long id);


// procurement-status--------------------
    ProcurementStatusDto createProcurementStatus(ProcurementStatusDto procurementStatusDto);
    List<ProcurementStatusDto> getProcurementStatus();
    ProcurementStatusDto getProcurementStatusById(Long id);
    ProcurementStatusDto updateProcurementStatus(Long id, ProcurementStatusDto procurementStatusDto);
    void deleteProcurementStatus(Long id);



// user----------------------------------------
    UserDto createUser(UserDto userDto);
    List<UserDto> getUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);

// subdiv----------------------------------------
    SubdivDto createSubdiv(SubdivDto subdivDto);
    List<SubdivDto> getSubdivs();
    SubdivDto getSubdivById(Long id);
    List<SubdivDto> getSubdivsByAdmindivId(Long id);
    SubdivDto updateSubdiv(Long id, SubdivDto subdivDto);
    void deleteSubdiv(Long id);

// admindiv----------------------------------------
    Admindiv createAdmindiv(Admindiv admindiv);
    List<Admindiv> getAllAdmindivs();

    Admindiv getAdmindivById(Long id);

    Admindiv updateAdmindiv(Long id, Admindiv admindiv);

    void deleteAdmindiv(Long id);


// designation ----------------------------------------

    Designation createDesignation(Designation designation);

    List<Designation> getAllDesignations();

    Designation getDesignationById(Long id);

    Designation updateDesignation(Long id, Designation designation);

    void deleteDesignation(Long id);
}