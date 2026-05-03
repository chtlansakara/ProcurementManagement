package com.cht.procurementManagement.services.attachment;

import com.cht.procurementManagement.entities.PDFAttachment;
import com.cht.procurementManagement.enums.EntityType;
import com.cht.procurementManagement.repositories.PDFAttachmentRepository;
import com.cht.procurementManagement.services.auth.AuthService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttachmentService {
    private final AttachmentStorageService attachmentStorageService;
    private final PDFAttachmentRepository pdfAttachmentRepository;
    private final AuthService authService;

    public AttachmentService(AttachmentStorageService attachmentStorageService,
                             PDFAttachmentRepository pdfAttachmentRepository,
                             AuthService authService) {
        this.attachmentStorageService = attachmentStorageService;
        this.pdfAttachmentRepository = pdfAttachmentRepository;
        this.authService = authService;
    }

    //save file
    public PDFAttachment uploadFile(MultipartFile file, String name, Long referenceId, EntityType referenceType) throws IOException {
        //validate the file
        validateFile(file, referenceType, referenceId);

        //get the path to service
        String storagePath;
        try(InputStream inputStream = file.getInputStream()){
            storagePath = attachmentStorageService.storeFile(inputStream, file.getOriginalFilename());
        }

        //create new db object
        PDFAttachment pdfAttachment = PDFAttachment.builder()
                .referenceType(referenceType)
                .referenceId(referenceId)
                .name(name)
                .originalName(file.getOriginalFilename())
                .storedPath(storagePath)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .createdAt(LocalDateTime.now())
                .userId(authService.getLoggedUserDto().getId())
                .build();

        //save to db
        return pdfAttachmentRepository.save(pdfAttachment);
    }

    //class method to validate file
    private void validateFile(MultipartFile file, EntityType referenceType, Long referenceId){
        if(file.isEmpty()){
            throw new IllegalArgumentException("File is empty!");
        }
        //Type check:
        String mimeType = file.getContentType();
        if(mimeType == null || !mimeType.equals("application/pdf")){
            throw new IllegalArgumentException("Invalid MIME Type!");
        }
        //Allowed limit check:
//        if(referenceType.equals(EntityType.REQUEST)){
//            pdfAttachmentRepository
//                    .findFirstByReferenceIdAndReferenceType(referenceId, referenceType)
//                    .ifPresent( existing -> {
//                        throw new IllegalArgumentException("A file already exists for this request");
//                    });
//        }
//
//        if(referenceType.equals(EntityType.APPROVAL)){
//            pdfAttachmentRepository
//                    .findFirstByReferenceIdAndReferenceType(referenceId, referenceType)
//                    .ifPresent( existing -> {
//                        throw new IllegalArgumentException("A file already exists for this approval");
//                    });
//        }
    }

    //method to download file
    public Resource getFileResource(Long fileId) throws IOException {
        //retrieve attachment object from db - using class method
        PDFAttachment retrievedAttachment = getAttachmentById(fileId);
        return attachmentStorageService.getFileAsResource(retrievedAttachment.getStoredPath());

    }

    //method to get all file details for a procurement
    public List<PDFAttachment> getAllAttachmentsByProcurement(Long procurementId) throws IOException {
        return pdfAttachmentRepository
                .findAllByReferenceIdAndReferenceType(procurementId, EntityType.PROCUREMENT);
    }

    //method to delete all files related to a procurement, request or an approval
    public void deleteAllAttachmentsOfAnEntity(Long referenceId, EntityType referenceType ) throws IOException {
        List<Long> attachmentIds = pdfAttachmentRepository
                .findAllByReferenceIdAndReferenceType(referenceId, referenceType)
                .stream()
                .map(PDFAttachment::getId)
                .toList();

        for (Long fileId: attachmentIds) {
            deleteAttachment(fileId);
        }
    }


    //method to retrieve file object from db by attachment id
    public PDFAttachment getAttachmentById(Long fileId) throws IOException{
        return pdfAttachmentRepository.findById(fileId)
                .orElseThrow( () -> new FileNotFoundException("File not found"));
    }


    //to get attachment info for request or approval by its id
    public Optional<PDFAttachment> getAttachment(Long id, EntityType entityType) throws FileNotFoundException {
        if(entityType.equals(EntityType.REQUEST)){
        return pdfAttachmentRepository.findFirstByReferenceIdAndReferenceType(id, EntityType.REQUEST);

        }
        if(entityType.equals(EntityType.APPROVAL)){
            return pdfAttachmentRepository.findFirstByReferenceIdAndReferenceType(id, EntityType.APPROVAL);

        }
        return Optional.empty();
    }

    //to delete attachment by attachment id
    public void deleteAttachment(Long fileId) throws IOException {
        //retrieve attachment object from db - using class method
        PDFAttachment existing = getAttachmentById(fileId);
        //delete from file directory
        attachmentStorageService.deleteFile(existing.getStoredPath());
        //delete from db
        pdfAttachmentRepository.deleteById(fileId);
    }

}
