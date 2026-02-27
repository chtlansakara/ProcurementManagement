package com.cht.procurementManagement.services.report;

import net.sf.jasperreports.engine.JRException;

import java.util.Date;

public interface SummaryReportService {

    //sending the report file to frontend
    byte[] generateSummaryReportWFormat(Date startDate, Date endDate, String fileFormat) throws JRException;

    //saved report in the backend
    String generateSummaryReport(Date startDate, Date endDate) throws Exception;

}

