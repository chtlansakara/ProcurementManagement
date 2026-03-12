package com.cht.procurementManagement.services.requests;

import com.cht.procurementManagement.dto.RequestDto;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.*;
import com.cht.procurementManagement.enums.AuditEntityType;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.RequestRepository;
import com.cht.procurementManagement.repositories.SubdivRepository;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Request Service - Unit Tests")
class RequestServiceImplTest {
    //service class being tested
    @InjectMocks
    private RequestServiceImpl requestService;

    //dependencies to mock
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SubdivRepository subdivRepository;
    @Mock
    private AuthService authService;
    @Mock
    private NotificationService notificationService;

    //re-used objects
    private Request request;
    private RequestDto requestDto;

    //other required objects
    private User loggedUser;
    private UserDto loggedUserDto;
    private Admindiv admindiv;
    private Subdiv subdiv;


    @BeforeEach
    void setup(){
        //initializing the objects re-used & other required objects
        //request need loggeduser object, subdiv List and admindiv objects

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
        admindiv = new Admindiv();
        admindiv.setId(1L);
        admindiv.setName("Faculty of Science");
        admindiv.setCode("FOS");
        admindiv.setResponsibleDesignation(designationForAdmindiv);


        //subdiv for request
        subdiv = new Subdiv();
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
        //dto
        requestDto = new RequestDto();
        requestDto.setTitle("Keyboards");
        requestDto.setQuantity("20");
        requestDto.setDescription("Any brand");
        requestDto.setFund("101");
        requestDto.setStatus(RequestStatus.PENDING_PROCUREMENT);
        requestDto.setPreviouslyPurchased(false);
        requestDto.setSubdivIdList(List.of(1L));


    }

    @Nested
    @DisplayName("Create Request Tests")
    class createRequestTests{

        //case 01 - success
        //case 02 - throws when logged loggedUser doesn't exist
        //case 03 - throws when sub-divs are empty
        //case 04 - throws when admin div for subdiv is not found


        @Test
        @DisplayName("Create Request - Successfully")
        void createRequest_Success(){

            //ARRANGE:
            //return the loggedUserDto object as the logged loggedUser
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);

            //returning the loggedUser object when finding the logged loggedUser with id
            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.of(loggedUser));

            when(subdivRepository.findAllById(requestDto.getSubdivIdList()))
                    .thenReturn(List.of(subdiv));

            when(requestRepository.save(any(Request.class)))
                    .thenReturn(request);

            //ACT:
            RequestDto result = requestService.createRequest(requestDto);

            //ASSERT:
            assertNotNull(result);
            assertEquals(requestDto.getTitle(), result.getTitle());
            assertEquals(RequestStatus.PENDING_PROCUREMENT, result.getStatus());

            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            verify(subdivRepository).findAllById(any());
            verify(requestRepository).save(any(Request.class));
            //notification saving
            verify(notificationService).onRequestSubmitted(request);
        }

        @Test
        @DisplayName("Create Request - Throws when logged user doesn't exist")
        void createRequest_ThrowsWhenUserDoesNotExist(){
            //ARRANGE:
            //return the loggedUserDto object as the logged loggedUser
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);

            //returning the loggedUser object when finding the logged loggedUser with id
            //as not found
            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.empty());

            //ACT & ASSERT:
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> requestService.createRequest(requestDto)
            );
            assertEquals("User not found!", exception.getMessage());

            //VERIFY:
            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            //not executed
            verify(subdivRepository, never()).findAllById(any());
            verify(requestRepository, never()).save(any(Request.class));
            //notification saving
            verify(notificationService, never()).onRequestSubmitted(request);
        }

        @Test
        @DisplayName("Create Request - Throws when subdiv list is empty")
        void createRequest_ThrowsWhenSubdivListIsEmpty(){
            //ARRANGE:
            //return the loggedUserDto object as the logged loggedUser
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);

            //returning the loggedUser object when finding the logged loggedUser with id
            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.of(loggedUser));

            //return an empty list
            when(subdivRepository.findAllById(requestDto.getSubdivIdList()))
                    .thenReturn(List.of());

            //ACT & ASSERT:
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> requestService.createRequest(requestDto)
            );
            assertEquals("Sub divisions for request are empty", exception.getMessage());

            //VERIFY:
            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            verify(subdivRepository).findAllById(any());
            //not executed
            verify(requestRepository, never()).save(any(Request.class));
            //notification saving
            verify(notificationService, never()).onRequestSubmitted(request);
        }


        @Test
        @DisplayName("Create Request - Throws when admindiv of subdiv is not found")
        void createRequest_ThrowsWhenAdmindivDoesNotExist(){
            //ARRANGE:
            //set the admindiv of the request subdiv list is null
            subdiv.setAdmindiv(null);

            //return the loggedUserDto object as the logged loggedUser
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);

            //returning the loggedUser object when finding the logged loggedUser with id
            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.of(loggedUser));

            when(subdivRepository.findAllById(requestDto.getSubdivIdList()))
                    .thenReturn(List.of(subdiv));

            //ACT & ASSERT:
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> requestService.createRequest(requestDto)
            );
            assertEquals("Admin division of request can not be found", exception.getMessage());

            //VERIFY:
            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            verify(subdivRepository).findAllById(any());
            //not executed
            verify(requestRepository, never()).save(any(Request.class));
            //notification saving
            verify(notificationService, never()).onRequestSubmitted(request);
        }

    }

    @Nested
    @DisplayName("Update Request Tests")
    class updateRequestTests{
        private RequestDto updatingDto;

        @BeforeEach
        void setup(){
            updatingDto = new RequestDto();
            updatingDto.setTitle("Keyboard");
            updatingDto.setQuantity("1");
            updatingDto.setDescription("Logitech");
            updatingDto.setFund("101");
            updatingDto.setStatus(RequestStatus.PENDING_PROCUREMENT);
            updatingDto.setPreviouslyPurchased(true);
            updatingDto.setSubdivIdList(List.of(1L));
        }

        //case 01 - successfully update
        //case 02 - user not found
        //case 03 - sub-divs don't exist
        @Test
        @DisplayName("Update Request - Successfully")
        void updateRequest_Success(){
            //ASSERT:
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);
            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.of(loggedUser));
            when(subdivRepository.findAllById(updatingDto.getSubdivIdList()))
                    .thenReturn(List.of(subdiv));
            when(requestRepository.save(any(Request.class)))
                    .thenReturn(request);

            //ACT:
            RequestDto result = requestService.updateRequest(request, updatingDto);

            //ASSERT:
            assertNotNull(result);
            assertNotNull(result.getLastUpdatedDate());
            assertEquals(loggedUser.getId(),result.getUserIdLastUpdatedBy());
            assertEquals(updatingDto.getTitle(), result.getTitle());
            assertEquals(updatingDto.getQuantity(), result.getQuantity());
            assertEquals(updatingDto.getDescription(), result.getDescription());
            assertEquals(updatingDto.getSubdivIdList(),result.getSubdivIdList());


            //VERIFY:
            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            verify(subdivRepository).findAllById(any());
            verify(requestRepository).save(any(Request.class));

        }

        @Test
        @DisplayName("Update Request - Throws when user doesn't exist")
        void updateRequest_ThrowsWhenUserDoesNotExist(){
            //ASSERT:
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);
            //user not found
            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.empty());

            //ACT & ASSERT:
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> requestService.updateRequest(request, updatingDto)
            );

            assertEquals("User not found", exception.getMessage());

            //VERIFY:
            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            //not executed
            verify(subdivRepository, never()).findAllById(any());
            verify(requestRepository, never()).save(any(Request.class));

        }

        @Test
        @DisplayName("Update Request - Throws when sub divs don't exist")
        void updateRequest_ThrowsWhenSubdivsDoNotExist(){
            //ASSERT:
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);
            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.of(loggedUser));
            //returning an empty list
            when(subdivRepository.findAllById(updatingDto.getSubdivIdList()))
                    .thenReturn(List.of());

            //ACT & ASSERT:
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> requestService.updateRequest(request, updatingDto)
            );

            assertEquals("Invalid sub-divisions!", exception.getMessage());

            //VERIFY:
            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            verify(subdivRepository).findAllById(updatingDto.getSubdivIdList());
            //not executed
            verify(requestRepository, never()).save(any(Request.class));

        }

    }

    @Nested
    @DisplayName("Get Request By Id Tests")
    class getRequestByIdTests{
        Long requestId = 1L;

        @Test
        @DisplayName("Get Request By Id - Successfully")
        void getRequestById_Success(){
            //ARRANGE
            when(requestRepository.findById(requestId))
                    .thenReturn(Optional.of(request));
            //ACT
            RequestDto result = requestService.getRequestById(requestId);

            //ASSERT:
            assertNotNull(result);
            assertEquals(requestId, result.getId());

            //VERIFY:
            verify(requestRepository).findById(requestId);

        }

        @Test
        @DisplayName("Get Request By Id - Throws when id doesn't exist")
        void getRequestById_ThrowsWhenRequestDoesNotExist(){
            //ARRANGE
            when(requestRepository.findById(requestId))
                    .thenReturn(Optional.empty());
            //ACT
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> requestService.getRequestById(requestId)
            );

            //ASSERT:
            assertEquals("Request not found",exception.getMessage());

            //VERIFY:
            verify(requestRepository).findById(requestId);
        }

    }


    @Nested
    @DisplayName("Delete Request Tests")
    class deleteRequestTests{
        @Test
        @DisplayName("Delete Request - Successfully")
        void deleteRequest_Success(){
            //ARRANGE
            doNothing().when(requestRepository).delete(request);

            //ACT
           requestService.deleteRequest(request);

            //VERIFY:
            verify(notificationService).deleteNotifications(AuditEntityType.REQUEST, request.getId());
            verify(requestRepository).delete(request);

        }



    }

}