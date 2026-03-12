package com.cht.procurementManagement.services.Comment;

import com.cht.procurementManagement.dto.CommentDto;
import com.cht.procurementManagement.dto.UserDto;
import com.cht.procurementManagement.entities.*;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.ReviewType;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.CommentRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Comment Service - Unit Tests")
class CommentServiceImplTest {

    //service class being tested
    @InjectMocks
    private CommentServiceImpl commentService;

    //dependencies
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthService authService;
    @Mock
    private NotificationService notificationService;

    //re-used objects
    private Comment comment;
    private CommentDto commentDto;
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

        //comment & commentDto
        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Approved to be included for 2027");
        comment.setType(ReviewType.ADMIN_DIV);
        comment.setAuthorizedBy("Asst. Registrar");
        comment.setCreatedDate(new Date());
        comment.setCreatedBy(loggedUser);
        comment.setRequest(request);
        //dto
        commentDto = new CommentDto();
        commentDto.setContent("Approved to be included for 2027");
        commentDto.setType(ReviewType.ADMIN_DIV);
        commentDto.setAuthorizedBy("Asst. Registrar");
        commentDto.setRequestId(request.getId());



    }


    @Nested
    @DisplayName("Create Comment Tests")
    class createCommentTests{
        //case 01 - success
        //case 02 - throws when logged user doesn't exist
        //case 03 - throws when request doesn't exist

        @Test
        @DisplayName("Create Comment - Successfully")
        void createComment_Success(){
            //ARRANGE:
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);
            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.of(loggedUser));
            when(requestRepository.findById(commentDto.getRequestId()))
                    .thenReturn(Optional.of(request));
            when(commentRepository.save(any(Comment.class)))
                    .thenReturn(comment);
            //ACT:
            CommentDto result = commentService.createComment(commentDto);

            //ASSERT:
            assertNotNull(result);
            assertNotNull(result.getCreatedDate());
            assertEquals(loggedUser.getId(), result.getCreatedByUserId());
            assertEquals(commentDto.getContent(), result.getContent());
            assertEquals(commentDto.getType(), result.getType());
            assertEquals(commentDto.getRequestId(), result.getRequestId());
            //VERIFY:
            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            verify(requestRepository).findById(commentDto.getRequestId());
            verify(commentRepository).save(any(Comment.class));
            verify(notificationService).onRequestRejection(request, comment.getType());

        }

        @Test
        @DisplayName("Create Comment - Throws when user doesn't exist")
        void createComment_ThrowsWhenUserDoesNotExist(){
            //ARRANGE:
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);
            //not found
            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.empty());


            //ACT & ASSERT:
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> commentService.createComment(commentDto)
            );

            assertEquals("User not found!", exception.getMessage());

            //VERIFY:
            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            //not executed
            verifyNoInteractions(requestRepository);
            verifyNoInteractions(commentRepository);
            verifyNoInteractions(notificationService);
        }
        @Test
        @DisplayName("Create Comment - Throws when request doesn't exist")
        void createComment_ThrowsWhenRequestDoesNotExist(){
            //ARRANGE:
            when(authService.getLoggedUserDto())
                    .thenReturn(loggedUserDto);

            when(userRepository.findById(loggedUserDto.getId()))
                    .thenReturn(Optional.of(loggedUser));
            //not found
            when(requestRepository.findById(commentDto.getRequestId()))
                    .thenReturn(Optional.empty());

            //ACT & ASSERT:
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> commentService.createComment(commentDto)
            );

            assertEquals("Request not found!", exception.getMessage());

            //VERIFY:
            verify(authService).getLoggedUserDto();
            verify(userRepository).findById(loggedUserDto.getId());
            verify(requestRepository).findById(commentDto.getRequestId());
            //not executed
            verifyNoInteractions(commentRepository);
            verifyNoInteractions(notificationService);

        }

    }

    @Nested
    @DisplayName("Get Comments By Request Id Tests")
    class getCommentsByRequestIdTests{
        Long requestId = 1L;

        //case 01 - success
        //case 02 - returns an empty list

        @Test
        @DisplayName("Get Comments By Request Id - Successfully")
        void getCommentsById_Success(){
            //ARRANGE
            //new comment
            Comment comment2 = new Comment();
            comment2.setId(2L);
            comment2.setContent("Already included");
            comment2.setType(ReviewType.SUPPLIES);
            comment2.setAuthorizedBy("Bursar");
            Date date = java.sql.Date.valueOf(LocalDate.of(2025, 1, 15));
            comment2.setCreatedDate(date);
            comment2.setCreatedBy(loggedUser);
            comment2.setRequest(request);

            List<Comment> commentList = new ArrayList<>();
            commentList.add(comment);
            commentList.add(comment2);

            when(commentRepository.findAllByRequestId(requestId))
                    .thenReturn(commentList);

            //ACT:
            List<CommentDto> result = commentService.getCommentsByRequestId(requestId);

            //ASSERT:
            assertNotNull(result);
            //check if sorted
            assertEquals(comment2.getId(), result.get(0).getId());
            assertEquals(comment.getId(), result.get(1).getId());

            //VERIFY:
            verify(commentRepository).findAllByRequestId(requestId);
        }


        @Test
        @DisplayName("Get Comments By Id - Returns an empty list")
        void getCommentsById_ReturnsEmptyList(){
            //ARRANGE
            when(commentRepository.findAllByRequestId(requestId))
                    .thenReturn(List.of());

            //ACT:
            List<CommentDto> result = commentService.getCommentsByRequestId(requestId);

            //ASSERT:
            assertNotNull(result);
            assertTrue(result.isEmpty());

            //VERIFY:
            verify(commentRepository).findAllByRequestId(requestId);
        }

    }


}