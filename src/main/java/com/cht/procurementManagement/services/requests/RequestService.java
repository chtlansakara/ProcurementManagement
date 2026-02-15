package com.cht.procurementManagement.services.requests;

import com.cht.procurementManagement.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(RequestDto requestDto);

    RequestDto updateRequest(Long id, RequestDto requestDto);
    List<RequestDto> getAllRequests();

    void deleteRequest(Long requestId);
}
