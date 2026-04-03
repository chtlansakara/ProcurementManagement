package com.cht.procurementManagement.services.report;

import com.cht.procurementManagement.dto.procurement.ProcurementReportDTO;
import com.cht.procurementManagement.dto.procurement.SummaryReportDTO;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.repositories.ProcurementRepository;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.admindiv.AdminDivService;
import com.cht.procurementManagement.services.subdiv.SubDivService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    //to get list we inject the employee repository
    private UserRepository userRepository;
    private ProcurementRepository procurementRepository;
    private SubDivService subDivService;
    private AdminDivService adminDivService;
    public ReportService(UserRepository userRepository,
                         ProcurementRepository procurementRepository,
                         SubDivService subDivService,
                         AdminDivService adminDivService) {
        this.userRepository = userRepository;
        this.procurementRepository = procurementRepository;
        this.subDivService = subDivService;
        this.adminDivService = adminDivService;
    }

    public String exportReport(String reportFormat) throws FileNotFoundException, JRException {

        //file location to save exported file
        String path = "D:\\Procurement Mangement - MIT Project 2026";

        List<User> users = userRepository.findAll();
        //load file
        File file = ResourceUtils.getFile("classpath:usersInfo.jrxml");
        //and compile it
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        //get the data source from our objects list
        JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(users);
        //fill the report with datasource
        //for parameters, create a map
        Map<String, Object> parameters = new HashMap<>();
        //add created by details
        parameters.put("createdBy", "voidcht");
        //then fill the report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);
        //by the given format, generate the report
        if(reportFormat.equalsIgnoreCase("html")){
            //generate html report in the given file location with file name
        JasperExportManager.exportReportToHtmlFile(jasperPrint,path +"\\usersInformation.html");
        }
        if(reportFormat.equalsIgnoreCase("pdf")){
            //generate pdf report
            JasperExportManager.exportReportToPdfFile(jasperPrint,path +"\\usersInformation.pdf");
        }
        //return a string message
        return "Report generated in path: "+ path;
    }

    public byte[] generateProcurementReportWFormat(Date startDate, Date endDate, String fileFormat) throws JRException {


        //1. get data from db
        List<ProcurementReportDTO> reportData = procurementRepository.findProcurementReportData(startDate,endDate);

        if(reportData.isEmpty()){
            throw new RuntimeException("There is no procurement data for that period");
        }

        //2. Load the .jrxml file from resources
        String reportPath = "/suppliesprocurement.jrxml";
        InputStream reportStream = getClass().getResourceAsStream(reportPath);
        if(reportStream == null){
            throw new RuntimeException("Report file not found at " +reportPath);
        }

        //3.compile the report
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        //4. Convert data to datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        //5. set parameters
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("createdOn", new Date() );
        parameters.put("START_DATE", startDate);
        parameters.put("END_DATE", endDate);

        //6. Fill Report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        //7. Export to pdf bytes and return
        if(fileFormat.equalsIgnoreCase("pdf")) {
            //export tp bytes
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }

        if(fileFormat.equalsIgnoreCase("excel")) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
            config.setOnePagePerSheet(false);
            config.setDetectCellType(true);
            config.setCollapseRowSpan(false);
            config.setIgnoreGraphics(false);
            exporter.setConfiguration(config);

            exporter.exportReport();
            return outputStream.toByteArray();
        }

        return null;

    }

    //report for admin-div user
    public byte[] generateAdmindivProcurementReportWFormat(Date startDate, Date endDate, String fileFormat) throws JRException {


        //1. get data from db
        List<ProcurementReportDTO> reportData = adminDivService.getAllProcurementForAdmindivReport(startDate, endDate);

        if(reportData.isEmpty()){
            throw new RuntimeException("There is no procurement data for that period");
        }

        //2. Load the .jrxml file from resources
        String reportPath = "/divisionprocurement.jrxml";
        InputStream reportStream = getClass().getResourceAsStream(reportPath);
        if(reportStream == null){
            throw new RuntimeException("Report file not found at " +reportPath);
        }

        //3.compile the report
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        //4. Convert data to datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        //5. set parameters
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("createdOn", new Date() );
        parameters.put("START_DATE", startDate);
        parameters.put("END_DATE", endDate);

        //6. Fill Report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        //7. Export to pdf bytes and return
        if(fileFormat.equalsIgnoreCase("pdf")) {
            //export tp bytes
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }

        if(fileFormat.equalsIgnoreCase("excel")) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
            config.setOnePagePerSheet(false);
            config.setDetectCellType(true);
            config.setCollapseRowSpan(false);
            config.setIgnoreGraphics(false);
            exporter.setConfiguration(config);

            exporter.exportReport();
            return outputStream.toByteArray();
        }

        return null;

    }


    //report for sub-div user
    public byte[] generateSubdivProcurementReportWFormat(Date startDate, Date endDate, String fileFormat) throws JRException {


        //1. get data from db
        List<ProcurementReportDTO> reportData = subDivService.getAllProcurementForSubdivReport(startDate, endDate);

        if(reportData.isEmpty()){
            throw new RuntimeException("There is no procurement data for that period");
        }

        //2. Load the .jrxml file from resources
        String reportPath = "/divisionprocurement.jrxml";
        InputStream reportStream = getClass().getResourceAsStream(reportPath);
        if(reportStream == null){
            throw new RuntimeException("Report file not found at " +reportPath);
        }

        //3.compile the report
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        //4. Convert data to datasource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        //5. set parameters
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("createdOn", new Date() );
        parameters.put("START_DATE", startDate);
        parameters.put("END_DATE", endDate);

        //6. Fill Report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        //7. Export to pdf bytes and return
        if(fileFormat.equalsIgnoreCase("pdf")) {
            //export tp bytes
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }

        if(fileFormat.equalsIgnoreCase("excel")) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
            config.setOnePagePerSheet(false);
            config.setDetectCellType(true);
            config.setCollapseRowSpan(false);
            config.setIgnoreGraphics(false);
            exporter.setConfiguration(config);

            exporter.exportReport();
            return outputStream.toByteArray();
        }

        return null;

    }


}
