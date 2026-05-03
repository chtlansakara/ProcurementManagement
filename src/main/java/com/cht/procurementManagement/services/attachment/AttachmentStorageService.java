package com.cht.procurementManagement.services.attachment;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class AttachmentStorageService {

    private final Path rootPath;

    //root path initialized from the environment variable
    public AttachmentStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.rootPath = Paths.get(uploadDir);
    }


    //saving the file to a local file directory
    public String storeFile(InputStream inputStream, String originalName) throws IOException {
        //for directory hierarchy
        LocalDate today = LocalDate.now();
        //for directory name: eg: ./2026/03/30
        Path dateDirectory = rootPath.resolve(
                today.getYear()+ File.separator+ String.format("%02d", today.getMonthValue())
                +File.separator+String.format("%02d", today.getDayOfMonth())
        );
        //create directory if it doesn't exist
        Files.createDirectories(dateDirectory);

        //creating storing path with new name:
        //get extension
        String extension = getFileExtension(originalName);
        //new name to store
        String storedName = UUID.randomUUID() + (extension.isEmpty() ? "" : "."+ extension);
        //store path
        Path filePath = dateDirectory.resolve(storedName);

        try(OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)) {
            //copy content to the new input stream
            StreamUtils.copy(inputStream, outputStream);
        }
        //return the stored path
        return rootPath.relativize(filePath).toString();
    }

    //private method to get file extension from the original name
    private String getFileExtension(String originalName){
        int lastDotPlace = originalName.lastIndexOf('.');
        return lastDotPlace == -1 ? "": originalName.substring(lastDotPlace+1);
    }

    //retrieving file from local file directory
    public Resource getFileAsResource(String storedPath) throws FileNotFoundException {
        Path filePath = rootPath.resolve(storedPath).normalize();

        //Security check:
        //convert to an absolute path
        Path normalizedRoot = rootPath.normalize().toAbsolutePath();
        if(!filePath.startsWith(normalizedRoot)){
            throw new SecurityException("Access denied!");
        }

        //Existence check:
        if(!Files.exists(filePath)){
            throw new FileNotFoundException("File not found!");
        }

        try {
            return new UrlResource(filePath.toUri());
        }catch(MalformedURLException e){
            throw new FileNotFoundException("Invalid file path: "+storedPath);
        }
    }

    //delete file from local file directory
    public Boolean deleteFile(String storedPath) throws IOException {
        Path filePath = rootPath.resolve(storedPath).normalize();

        //Security check:
        //convert to an absolute path
        Path normalizedRoot = rootPath.normalize().toAbsolutePath();
        if(!filePath.startsWith(normalizedRoot)){
            throw new SecurityException("Access denied!");
        }

        //Existence check:
        if(!Files.exists(filePath)){
            throw new FileNotFoundException("File not found!");
        }

        return Files.deleteIfExists(filePath);
    }

}
