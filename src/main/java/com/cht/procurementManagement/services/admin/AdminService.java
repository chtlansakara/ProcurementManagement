package com.cht.procurementManagement.services.admin;

import com.cht.procurementManagement.dto.*;
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
    AdmindivDto createAdmindiv(AdmindivDto admindivDto);
    List<AdmindivDto> getAllAdmindivs();

    AdmindivDto getAdmindivById(Long id);

    AdmindivDto updateAdmindiv(Long id, AdmindivDto admindivDto);

    void deleteAdmindiv(Long id);


// designation ----------------------------------------

    Designation createDesignation(Designation designation);

    List<Designation> getAllDesignations();

    Designation getDesignationById(Long id);

    Designation updateDesignation(Long id, Designation designation);

    void deleteDesignation(Long id);
}