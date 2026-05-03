package com.cht.procurementManagement.controllers.subdiv;

import com.cht.procurementManagement.dto.ProcurementStatusDto;
import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.services.procurement.ProcurementService;
import com.cht.procurementManagement.services.report.ReportService;
import com.cht.procurementManagement.services.subdiv.SubDivService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/subdiv")
@CrossOrigin("*")
public class SubdivUserController {
    private final SubDivService subDivService;
    private final ProcurementService procurementService;
    private final ReportService reportService;


    public SubdivUserController(SubDivService subDivService,
                                ProcurementService procurementService,
                                ReportService reportService) {
        this.subDivService = subDivService;

        this.procurementService = procurementService;
        this.reportService = reportService;
    }


    //print-request
    @GetMapping("/print-request/{requestId}")
    public ResponseEntity<byte[]> generateRequestPrint(@PathVariable Long requestId) {
        String format = "pdf";

        try {
            byte[] file = reportService.generatePrintRequestReport(requestId, format);
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
                fileName = "print_request.pdf";
            }else if(format.equalsIgnoreCase("excel")){
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName = "print_request.xlsx";
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



    //sub-div procurement report
    @GetMapping("/procurement-report/")
    public ResponseEntity<byte[]> generateProcurementReportWFormat(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam (defaultValue = "pdf") String format) {

        try {
            byte[] file = reportService.generateSubdivProcurementReportWFormat(startDate, endDate, format);
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
                fileName = "subdivision_procurement_report.pdf";
            }else if(format.equalsIgnoreCase("excel")){
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName = "subdivision_procurement_report.xlsx";
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

    //request report for subdiv
    @GetMapping("/request-report/")
    public ResponseEntity<byte[]> generateSubdivRequestReportWFormat(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam (defaultValue = "pdf") String format) {

        try {
            byte[] file = reportService.generateSubdivRequestReportWFormat(startDate, endDate, format);
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
                fileName = "subdivision_request_report.pdf";
            }else if(format.equalsIgnoreCase("excel")){
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName = "subdivision_request_report.xlsx";
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


    //get procurement of subdiv
    @GetMapping("/procurement")
    public ResponseEntity<?> getProcurementOfSubdiv(){
        try {
            return ResponseEntity.ok(subDivService.getAllProcurementOnlyBySubdivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get procurement by id
    @GetMapping("/procurement/{id}")
    public ResponseEntity<?> getProcurementOfSubdivById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(subDivService. getProcurementByIdForSubdiv(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get procurement status list
    @GetMapping("/procurement-status")
    public ResponseEntity<List<ProcurementStatusDto>> getProcurementStatus(){

        return ResponseEntity.ok(procurementService.getProcurementStatusList());
    }

    //get requests list for procurement
    @GetMapping("/procurement-requests")
    public ResponseEntity<List<RequestDto>> getRequestsForUpdateProcurement(){
        return ResponseEntity.ok(procurementService.getRequestsForUpdateProcurement());
    }

    //get status -updates for a procurement
    @GetMapping("/procurement-status/{id}")
    public ResponseEntity<?> getStatusUpdates(@PathVariable Long id){
        return ResponseEntity.ok(procurementService.getStatusUpdatesByProcurementId(id));
    }

    //get-stages list for filter
    @GetMapping("/procurement-stages")
    public ResponseEntity<List<String>> getProcurementStages(){
        return ResponseEntity.ok(procurementService.getProcurmentStagesList());
    }

//    request related.......


    //get all RELATED sub-div requests
    @GetMapping("/requests/related")
    public ResponseEntity<?> getRelatedRequestsBySubdiv(){
        try {
            return ResponseEntity.ok(subDivService.getAllRequestsRelatedBySubdivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get only all single sub-div requests
    @GetMapping("/requests")
    public ResponseEntity<?> getRequestsOnlyBySubdiv(){
        try {
            return ResponseEntity.ok(subDivService.getAllRequestsOnlyBySubdivId());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //create request for sub-div
    @PostMapping(value = "/requests", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRequestForSubdiv(@RequestPart("request") RequestDto requestDto,
                                                    @RequestPart(value ="file", required = false) MultipartFile file){
        //save to db
        RequestDto createdRequestDto = subDivService.createRequestBySubdiv(requestDto, file);
        if(createdRequestDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request couldn't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestDto);
    }
//    public ResponseEntity<?> createRequestForSubdiv(@RequestBody RequestDto requestDto){
//        //save to db
//        RequestDto createdRequestDto = subDivService.createRequestBySubdiv(requestDto);
//        if(createdRequestDto == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request couldn't be created");
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestDto);
//    }

    //get request by id
    @GetMapping("/requests/{id}")
    public ResponseEntity<?> findRequestById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(subDivService.getRequestById(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get sub div by id
    @GetMapping("/subdiv-info")
    public ResponseEntity<?> findSubdivById(){
        try {
            return ResponseEntity.ok(subDivService.getSubdiv());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get comments by request id
    @GetMapping("/requests/comments/{id}")
    public ResponseEntity<?> getCommentsByRequestId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(subDivService.getCommentsByRequestId(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //get approval by request id
    @GetMapping("/requests/approvals/{id}")
    public ResponseEntity<?> getApprovalsByRequestId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(subDivService.getApprovalsByRequestId(id));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //delete request
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id){
        try {
            subDivService.deleteRequestById(id);
            return ResponseEntity.ok(null);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //update request
    @PutMapping("/requests/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Long id, @RequestBody RequestDto requestDto){
        RequestDto updatedRequestDto = subDivService.updateRequestById(id, requestDto);
        if(updatedRequestDto ==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRequestDto);
    }

}
