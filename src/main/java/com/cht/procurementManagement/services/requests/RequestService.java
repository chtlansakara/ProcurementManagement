package com.cht.procurementManagement.services.requests;

import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.entities.Request;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(RequestDto requestDto);

    RequestDto updateRequest(Long id, RequestDto requestDto);

    RequestDto updateRequest(Request request, RequestDto requestDto);

    RequestDto getRequestById( Long requestId);

    List<RequestDto> getAllRequests();

    void deleteRequest(Long requestId);

    void deleteRequest (Request request);
}
