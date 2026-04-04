package com.cht.procurementManagement.controllers.supplies;

import com.cht.procurementManagement.services.report.ReportService;
import com.cht.procurementManagement.services.report.SummaryReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/supplies")
@CrossOrigin("*")
public class ReportController {

    private final SummaryReportService summaryReportService;
    private final ReportService reportService;
    public ReportController(SummaryReportService summaryReportService, ReportService reportService) {
        this.summaryReportService = summaryReportService;
        this.reportService = reportService;
    }

    //downloaded to frontend
    @GetMapping("/summary-report/")
    public ResponseEntity<byte[]> generateProcurementSummaryReportWFormat(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam (defaultValue = "pdf") String format) {

        try {
            byte[] file = summaryReportService.generateSummaryReportWFormat(startDate, endDate, format);
            //if not successful
            if(file == null) {
                return ResponseEntity.badRequest()
                        .body(("Unsupported format: " + format).getBytes());
            }
            //set content type and file name based on format
            String contentType;
            String fileName;
            if (format.equalsIgnoreCase("pdf")) {
                contentType = "application/pdf";
                fileName = "summary_report.pdf";
            }else if(format.equalsIgnoreCase("excel")){
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName = "summary_report.xlsx";
            }else{
                return ResponseEntity.badRequest()
                        .body(("Unsupported format: " + format).getBytes());
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" +fileName)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(file);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/procurement-report/")
    public ResponseEntity<byte[]> generateProcurementReportWFormat(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam (defaultValue = "pdf") String format) {

        try {
            byte[] file = reportService.generateProcurementReportWFormat(startDate, endDate, format);
            //if not successful
            if(file == null) {
                return ResponseEntity.badRequest()
                        .body(("Unsupported format: " + format).getBytes());
            }
            //set content type and file name based on format
            String contentType;
            String fileName;
            if (format.equalsIgnoreCase("pdf")) {
                contentType = "application/pdf";
                fileName = "procurement_report.pdf";
            }else if(format.equalsIgnoreCase("excel")){
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName = "procurement_report.xlsx";
            }else{
                return ResponseEntity.badRequest()
                        .body(("Unsupported format: " + format).getBytes());
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" +fileName)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(file);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



    //supplies request report
    @GetMapping("/request-report/")
    public ResponseEntity<byte[]> generateRequestReportWFormat(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam (defaultValue = "pdf") String format) {

        try {
            byte[] file = reportService.generateSuppliesRequestReportWFormat(startDate, endDate, format);
            //if not successful
            if(file == null) {
                return ResponseEntity.badRequest()
                        .body(("Unsupported format: " + format).getBytes());
            }
            //set content type and file name based on format
            String contentType;
            String fileName;
            if (format.equalsIgnoreCase("pdf")) {
                contentType = "application/pdf";
                fileName = "request_report.pdf";
            }else if(format.equalsIgnoreCase("excel")){
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName = "request_report.xlsx";
            }else{
                return ResponseEntity.badRequest()
                        .body(("Unsupported format: " + format).getBytes());
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" +fileName)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(file);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }






    //created in backend
    @GetMapping("/procurement-summary")
    public ResponseEntity<String> getProcurementSummaryReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        try {
            System.out.println(startDate);
            System.out.println(endDate);
            return ResponseEntity.ok().body( summaryReportService.generateSummaryReport(startDate, endDate));


        } catch (Exception e) {
//            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }



    }
