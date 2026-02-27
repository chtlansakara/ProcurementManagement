package com.cht.procurementManagement.services.procurement;

import com.cht.procurementManagement.dto.*;
import com.cht.procurementManagement.dto.procurement.ProcurementCreateDto;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.dto.procurement.ProcurementStatusUpdateDto;

import java.util.List;

public interface ProcurementService {




    //getting users for assignedTo - to create a procurement of update
    List<UserDto> getAssignedToUsersList();

    //getting vendors list - to create a procurement or update
    List<VendorDto> getVendorsList();

    //create a procurement
    ProcurementResponseDto createProcurement(ProcurementCreateDto createDto);

    //get all procurement
    List<ProcurementResponseDto> getProcurement();

    //get procurement by id
    ProcurementResponseDto getProcurementById(Long id);

    //update procurement
    ProcurementResponseDto updateProcurement(Long id, ProcurementCreateDto createDto);

    //update status of a procurement, creating a new status-update object
    ProcurementStatusUpdateDto updateStatus(Long procurementId, ProcurementStatusUpdateDto updateStatusDto);

    //get status-updates objects for a procurement
    List<ProcurementStatusUpdateDto> getStatusUpdatesByProcurementId(Long procurementId);

    //delete procurement by id
    void deleteProcurement(Long id);

    //get requests that can be chosen when updating a procurement
    List<RequestDto> getRequestsForUpdateProcurement();


    //getting procurement status objects - to update status of a procurement
    List<ProcurementStatusDto> getProcurementStatusList();

    //getting procurement stages list - to update status of a procurement
    List<String> getProcurmentStagesList();
    //getting procurement sources list - to update and create procurement
    List<ProcurementSourceDto> getProcurementSources();

}
