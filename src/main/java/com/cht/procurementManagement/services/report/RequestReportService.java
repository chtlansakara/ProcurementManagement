package com.cht.procurementManagement.services.report;

import com.cht.procurementManagement.dto.RequestReportDTO;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.repositories.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service

public class RequestReportService {
    private final RequestRepository requestRepository;

    public RequestReportService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }


    //for supplies users
    @Transactional(readOnly = true)
    public List<RequestReportDTO> getRequestReportData(Date startDate, Date endDate){
        List<RequestReportDTO> reportDTOS = requestRepository.findRequestReportData(startDate, endDate);

        //finding & adding relevant subdivisions to each request
        reportDTOS.forEach(dto -> {
            List<Subdiv> subdivs = requestRepository.findSubdivsByRequestId(dto.getId());
            if(!subdivs.isEmpty()){
                String subdivNames = subdivs.stream()
                        .map(Subdiv::getName)
                        .collect(Collectors.joining(", "));
                dto.setSubdivisions(subdivNames);
            }else{
                dto.setSubdivisions("N/A");
            }
        });

        return reportDTOS;
    }





}
