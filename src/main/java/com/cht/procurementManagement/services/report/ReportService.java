package com.cht.procurementManagement.services.report;

import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.repositories.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    //to get list we inject the employee repository
    private UserRepository userRepository;
    public ReportService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
