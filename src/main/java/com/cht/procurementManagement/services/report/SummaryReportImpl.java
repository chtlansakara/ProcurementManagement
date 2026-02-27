package com.cht.procurementManagement.services.report;

import com.cht.procurementManagement.dto.procurement.SummaryReportDTO;
import com.cht.procurementManagement.repositories.ProcurementRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class SummaryReportImpl implements SummaryReportService{
    private final ProcurementRepository procurementRepository;
    public SummaryReportImpl(ProcurementRepository procurementRepository) {
        this.procurementRepository = procurementRepository;
    }

    @Override
    public byte[] generateSummaryReportWFormat(Date startDate, Date endDate, String fileFormat) throws JRException {
        //file location to save exported file
        String path = "D:\\Procurement Mangement - MIT Project 2026";

        //1. get data from db
        List<SummaryReportDTO> reportData = procurementRepository.findSummaryReportData(startDate,endDate);

        if(reportData.isEmpty()){
            throw new RuntimeException("There is no procurement data for that period");
        }

        //2. Load the .jrxml file from resources
        String reportPath = "/summaryReport.jrxml";
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






    //created in the give file path (backend)
    @Override
    public String generateSummaryReport(Date startDate, Date endDate) throws Exception {
        //file location to save exported file
        String path = "D:\\Procurement Mangement - MIT Project 2026";

        //1. get data from db
        List<SummaryReportDTO> reportData = procurementRepository.findSummaryReportData(startDate,endDate);

        // Add this temporarily to verify data is returned
        System.out.println("Report data size: " + reportData.size());
        if (!reportData.isEmpty()) {
            System.out.println("First row: " + reportData.get(0).getAdminDivision()
                    + " | " + reportData.get(0).getProcurementStage()
                    + " | " + reportData.get(0).getEstimatedAmount());
        }

        //2. Load the .jrxml file from resources
        String reportPath = "/summaryReport.jrxml";
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
        parameters.put("START_DATE", startDate);
        parameters.put("END_DATE", endDate);

        //6. Fill Report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        //7. Export to pdf bytes and return
        JasperExportManager.exportReportToPdfFile(jasperPrint, path +"\\summaryReport.pdf");

        return "Report generated in path "+path;
    }
}
