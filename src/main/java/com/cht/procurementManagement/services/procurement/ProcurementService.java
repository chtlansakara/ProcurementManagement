package com.cht.procurementManagement.services.procurement;

import com.cht.procurementManagement.dto.ProcurementStatusDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.dto.VendorDto;
import com.cht.procurementManagement.dto.procurement.ProcurementCreateDto;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.dto.procurement.ProcurementStatusUpdateDto;

import java.util.List;

public interface ProcurementService {

    List<ProcurementStatusDto> getProcurementStatusList();
    List<UserDto> getAssignedToUsersList();
    List<VendorDto> getVendorsList();
    ProcurementResponseDto createProcurement(ProcurementCreateDto createDto);

    List<ProcurementResponseDto> getProcurement();
    ProcurementResponseDto getProcurementById(Long id);

    ProcurementResponseDto updateProcurement(Long id, ProcurementCreateDto createDto);

    ProcurementStatusUpdateDto updateStatus(Long procurementId, ProcurementStatusUpdateDto updateStatusDto);

    List<ProcurementStatusUpdateDto> getStatusUpdatesByProcurementId(Long procurementId);
    void deleteProcurement(Long id);

    List<RequestDto> getRequestsForUpdateProcurement();

}
