package com.cht.procurementManagement.services.Approval;

import com.cht.procurementManagement.dto.ApprovalDto;
import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.*;
import com.cht.procurementManagement.enums.ApprovalType;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.ReviewType;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.ApprovalRepository;
import com.cht.procurementManagement.repositories.RequestRepository;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.auth.AuthService;
import com.cht.procurementManagement.services.notification.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("Approval Service - Unit Tests")
class ApprovalServiceImplTest {

    //service class being tested
    @InjectMocks
    private ApprovalServiceImpl approvalService;

    @Mock
    private ApprovalRepository approvalRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthService authService;
    @Mock
    private NotificationService notificationService;
    //re-used objects
     private Approval approval;
     private ApprovalDto approvalDto;
     private Request request;
     private User loggedUser;
     private UserDto loggedUserDto;

     //initialization of re-used objects
     @BeforeEach
     void setup(){
         //need-> loggedUser (User & dto), request
         //designation for logged user
         Designation designationForLoggedUser = new Designation();
         designationForLoggedUser.setId(2L);
         designationForLoggedUser.setTitle("Supplies Clerk");
         designationForLoggedUser.setCode("SF");

         //designation (needed for admindiv)
         Designation designationForAdmindiv = new Designation();
         designationForAdmindiv.setId(2L);
         designationForAdmindiv.setTitle("Assistant Registrar");
         designationForAdmindiv.setCode("AR");


         //admindiv for logged user
         Admindiv admindivForLoggedUser = new Admindiv();
         admindivForLoggedUser.setId(2L);
         admindivForLoggedUser.setName("Administration");
         admindivForLoggedUser.setCode("ADMIN");
         admindivForLoggedUser.setResponsibleDesignation(designationForAdmindiv);


         //subdiv for logged user
         Subdiv subdivForLoggedUser = new Subdiv();
         subdivForLoggedUser.setId(1L);
         subdivForLoggedUser.setName("Supplies Division");
         subdivForLoggedUser.setCode("SUP-ADMIN");
         subdivForLoggedUser.setAdmindiv(admindivForLoggedUser);

         //loggedUser & loggedUserDto
         loggedUser = new User();
         loggedUser.setId(1L);
         loggedUser.setEmail("danu@gmail.com");
         loggedUser.setUserRole(UserRole.SUPPLIESUSER);
         loggedUser.setDesignation(designationForLoggedUser);
         loggedUser.setSubdiv(subdivForLoggedUser);
         loggedUser.setAdmindiv(admindivForLoggedUser);
         //dto
         loggedUserDto = new UserDto();
         loggedUserDto.setId(1L);

         //admindiv for request
         Admindiv admindiv = new Admindiv();
         admindiv.setId(1L);
         admindiv.setName("Faculty of Science");
         admindiv.setCode("FOS");
         admindiv.setResponsibleDesignation(designationForAdmindiv);


         //subdiv for request
         Subdiv subdiv = new Subdiv();
         subdiv.setId(1L);
         subdiv.setName("Department of Chemistry");
         subdiv.setCode("DOC-FOS");
         subdiv.setAdmindiv(admindiv);


         //request & requestDto
         request = new Request();
         request.setId(1L);
         request.setTitle("Keyboards");
         request.setStatus(RequestStatus.PENDING_PROCUREMENT);
         request.setCreatedBy(loggedUser);
         request.setSubdivList(List.of(subdiv));
         request.setAdmindiv(admindiv);
         request.setCreatedDate(new Date());

         //approval & dto
         approval = new Approval();
         approval.setId(1L);
         approval.setAllocatedAmount(BigDecimal.valueOf(40000));
         approval.setComment("Approved for 2027");
         approval.setFund("102");
         approval.setType(ApprovalType.ADMIN_DIV);
         approval.setAuthroizedBy("Asst. Registrar");
         Date date = java.sql.Date.valueOf(LocalDate.of(2025, 1, 15));
         approval.setApprovedDate(date);
         approval.setCreatedDate(new Date());
         approval.setCreatedBy(loggedUser);
         approval.setRequest(request);
         //dto
         approvalDto = new ApprovalDto();
         approvalDto.setAllocatedAmount(BigDecimal.valueOf(40000));
         approvalDto.setComment("Approved for 2027");
         approvalDto.setFund("102");
         approvalDto.setType(ApprovalType.ADMIN_DIV);
         approvalDto.setAuthorizedBy("Asst. Registrar");
         approvalDto.setApprovedDate(date);
         approvalDto.setRequestId(request.getId());
     }

     @Nested
    @DisplayName("Create Approval Tests")
    class createApprovalTests{
         //case 01 - success
         //case 02 - throws when user doesn't exist
         //case 03 - throws when request doesn't exist

         @Test
         @DisplayName("Create Approval - Successfully")
         void createApproval_Success() {
             //ARRANGE:
             when(authService.getLoggedUserDto())
                     .thenReturn(loggedUserDto);
             when(userRepository.findById(loggedUserDto.getId()))
                     .thenReturn(Optional.of(loggedUser));
             when(requestRepository.findById(approvalDto.getRequestId()))
                     .thenReturn(Optional.of(request));
             when(approvalRepository.save(any(Approval.class)))
                     .thenReturn(approval);
             //ACT:
             ApprovalDto result = approvalService.createApproval(approvalDto);

             //ASSERT:
             assertNotNull(result);
             assertNotNull(result.getId());
             assertNotNull(result.getCreatedDate());
             assertEquals(loggedUser.getId(), result.getCreatedByUserId());
             assertEquals(approvalDto.getComment(), result.getComment());
             assertEquals(approvalDto.getType(), result.getType());
             assertEquals(approvalDto.getRequestId(), result.getRequestId());
             assertEquals(approvalDto.getApprovedDate(), result.getApprovedDate());
             //VERIFY:
             verify(authService).getLoggedUserDto();
             verify(userRepository).findById(loggedUserDto.getId());
             verify(requestRepository).findById(approvalDto.getRequestId());
             verify(approvalRepository).save(any(Approval.class));
             verify(notificationService).onRequestApproval(request, approval.getType());
         }

        @Test
         @DisplayName("Create Approval - Throws when user doesn't exist")
         void createApproval_ThrowsWhenUserDoesNotExist(){
             //ARRANGE:
             when(authService.getLoggedUserDto())
                     .thenReturn(loggedUserDto);
             //not found
             when(userRepository.findById(loggedUserDto.getId()))
                     .thenReturn(Optional.empty());


             //ACT & ASSERT:
             EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                     () -> approvalService.createApproval(approvalDto)
             );

             assertEquals("User not found!", exception.getMessage());

             //VERIFY:
             verify(authService).getLoggedUserDto();
             verify(userRepository).findById(loggedUserDto.getId());
             //not executed
             verifyNoInteractions(requestRepository);
             verifyNoInteractions(approvalRepository);
             verifyNoInteractions(notificationService);
         }

         @Test
         @DisplayName("Create Approval - Throws when request doesn't exist")
         void createApproval_ThrowsWhenRequestDoesNotExist(){
             //ARRANGE:
             when(authService.getLoggedUserDto())
                     .thenReturn(loggedUserDto);

             when(userRepository.findById(loggedUserDto.getId()))
                     .thenReturn(Optional.of(loggedUser));
             //not found
             when(requestRepository.findById(approvalDto.getRequestId()))
                     .thenReturn(Optional.empty());

             //ACT & ASSERT:
             EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                     () -> approvalService.createApproval(approvalDto)
             );

             assertEquals("Request not found!", exception.getMessage());

             //VERIFY:
             verify(authService).getLoggedUserDto();
             verify(userRepository).findById(loggedUserDto.getId());
             verify(requestRepository).findById(approvalDto.getRequestId());
             //not executed
             verifyNoInteractions(approvalRepository);
             verifyNoInteractions(notificationService);

         }


     }

    @Nested
    @DisplayName("Get Approvals By Request Id Tests")
    class getApprovalsByRequestIdTests{
         //case 01 - success
         //case 02 - returns empty list

        Long requestId = 1L;
        @Test
        @DisplayName("Get Approvals By Request Id - Successfully")
        void getApprovalsById_Success(){
            //ARRANGE
            //new approval
            Approval approval2 = new Approval();
            approval2.setId(2L);
            approval2.setComment("No comment");
            approval2.setType(ApprovalType.SUPPLIES);
            approval2.setAuthroizedBy("Bursar");
            Date date = java.sql.Date.valueOf(LocalDate.of(2025, 1, 15));
            approval2.setCreatedDate(date);
            approval2.setCreatedBy(loggedUser);
            approval2.setRequest(request);

            List<Approval> approvalList = new ArrayList<>();
            approvalList.add(approval2);
            approvalList.add(approval);


            when(approvalRepository.findAllByRequestId(requestId))
                    .thenReturn(approvalList);

            //ACT:
            List<ApprovalDto> result = approvalService.getApprovalsByRequestId(requestId);

            //ASSERT:
            assertNotNull(result);
            //check if sorted
            assertEquals(approval.getId(), result.get(0).getId());
            assertEquals(approval2.getId(), result.get(1).getId());

            //VERIFY:
            verify(approvalRepository).findAllByRequestId(requestId);
        }


        @Test
        @DisplayName("Get Approvals By Id - Returns an empty list")
        void getApprovalsById_ReturnsEmptyList(){
            //ARRANGE
            when(approvalRepository.findAllByRequestId(requestId))
                    .thenReturn(List.of());

            //ACT:
            List<ApprovalDto> result = approvalService.getApprovalsByRequestId(requestId);

            //ASSERT:
            assertNotNull(result);
            assertTrue(result.isEmpty());

            //VERIFY:
            verify(approvalRepository).findAllByRequestId(requestId);
        }


    }

}