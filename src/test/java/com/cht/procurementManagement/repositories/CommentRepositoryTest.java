package com.cht.procurementManagement.repositories;

import com.cht.procurementManagement.entities.*;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.ReviewType;
import com.cht.procurementManagement.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;NON_KEYWORDS=USER",
        "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
})
@DisplayName("Comment Repository - Unit tests")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private AdmindivRepository admindivRepository;

    @Autowired
    private SubdivRepository subdivRepository;

    private Comment comment;
    private Request savedRequest;
    private User savedCreatedByUser;
    private Subdiv savedSubdivForRequest;
    private Admindiv savedAdmindivForRequest;

    @BeforeEach
    void setup(){
        //ARRANGE:
        Designation designationForUser = Designation.builder()
                .title("Supplies Clerk")
                .code("SF")
                .build();
        Designation savedDesignationForUser = designationRepository.save(designationForUser);

        Designation designationForAdmindiv = Designation.builder()
                .title("Assitant Registrar")
                .code("AR")
                .build();
        Designation savedDesignationForAdmindiv = designationRepository.save(designationForAdmindiv);

        Admindiv admindivForUser = Admindiv.builder()
                .name("Administration")
                .code("ADMIN")
                .responsibleDesignation(savedDesignationForAdmindiv)
                .build();
        Admindiv savedAdmindivForUser = admindivRepository.save(admindivForUser);

        Admindiv admindivForRequest = Admindiv.builder()
                .name("Faculty of Science")
                .code("FOS")
                .responsibleDesignation(savedDesignationForAdmindiv)
                .build();
        savedAdmindivForRequest = admindivRepository.save(admindivForRequest);

        Subdiv subdivForUser= Subdiv.builder()
                .name("Supplies Division")
                .code("SUP-ADMIN")
                .admindiv(savedAdmindivForUser)
                .build();
        Subdiv savedSubdivForUser = subdivRepository.save(subdivForUser);

        Subdiv subdivForRequest = Subdiv.builder()
                .name("Department of Biology")
                .code("FOS-DOB")
                .admindiv(savedAdmindivForRequest)
                .build();
        savedSubdivForRequest = subdivRepository.save(subdivForRequest);

        User createdByUser = User.builder()
                .name("Sarath")
                .email("sarath@gmail.com")
                .userRole(UserRole.SUPPLIESUSER)
                .designation(savedDesignationForUser)
                .subdiv(savedSubdivForUser)
                .admindiv(savedAdmindivForUser)
                .build();
        savedCreatedByUser = userRepository.save(createdByUser);

        Request request = Request.builder()
                .title("Keyboards")
                .status(RequestStatus.PENDING_PROCUREMENT)
                .createdBy(savedCreatedByUser)
                .subdivList(List.of(savedSubdivForRequest))
                .admindiv(savedAdmindivForRequest)
                .createdDate(new Date())
                .build();
        savedRequest = requestRepository.save(request);


        comment = Comment.builder()
                .content("can not proceed this time")
                .authorizedBy("Assistant Bursar")
                .type(ReviewType.SUPPLIES)
                .request(savedRequest)
                .createdBy(savedCreatedByUser)
                .createdDate(new Date())
                .build();

    }

    @Test
    @DisplayName("Save Comment Method")
    public void saveCommentTest() {

        //ACT:
        Comment savedComment = commentRepository.save(comment);

        //ASSERT:
        assertNotNull(savedComment);
        assertNotNull(savedComment.getId());
    }

    @Test
    @DisplayName("Find All Comments Method")
    public void findAllCommentsTest(){
        //ARRANGE:
        //creating another comment
        Date date2025 = java.sql.Date.valueOf(LocalDate.of(2025,10,28));
        Comment comment2 = Comment.builder()
                .content("can not proceed")
                .authorizedBy("DEAN")
                .type(ReviewType.ADMIN_DIV)
                .request(savedRequest)
                .createdBy(savedCreatedByUser)
                .createdDate(date2025)
                .build();
        //saving both comments
        commentRepository.save(comment);
        commentRepository.save(comment2);

        //ACT:
        List<Comment> commentsList = commentRepository.findAll();

        //ASSERT:
        assertNotNull(commentsList);
        assertEquals(2, commentsList.size());
    }

    @Test
    @DisplayName("Find By Id Comment Method")
    public void findByIdCommentTest(){
        //ARRANGE:
        //saving comment first
       commentRepository.save(comment);

        //ACT:
        Comment retrievedComment = commentRepository.findById(comment.getId()).get();

        //ASSERT:
        assertNotNull(retrievedComment);
        assertNotNull(retrievedComment.getId());

    }

    //custom query -> List<Comment> findAllByRequestId(Long requestId);
    @Test
    @DisplayName("Find All Comments By Request Id  Method")
    public void findAllCommentsByRequestIdTest(){
        //ARRANGE:
        //creating another comment
        Date date2025 = java.sql.Date.valueOf(LocalDate.of(2025,10,28));
        Comment comment2 = Comment.builder()
                .content("can not proceed")
                .authorizedBy("DEAN")
                .type(ReviewType.ADMIN_DIV)
                .request(savedRequest)
                .createdBy(savedCreatedByUser)
                .createdDate(date2025)
                .build();

        //creating another comment with another request
        //a new request
        Request request2 = Request.builder()
                .title("Keyboards")
                .status(RequestStatus.PENDING_PROCUREMENT)
                .createdBy(savedCreatedByUser)
                .subdivList(List.of(savedSubdivForRequest))
                .admindiv(savedAdmindivForRequest)
                .createdDate(new Date())
                .build();
        Request savedRequest2 = requestRepository.save(request2);

        //comment with another request
        Comment comment3 = Comment.builder()
                .content("can not proceed")
                .authorizedBy("DEAN")
                .type(ReviewType.ADMIN_DIV)
                .request(savedRequest2)
                .createdBy(savedCreatedByUser)
                .createdDate(date2025)
                .build();

        //saving all comments
        commentRepository.save(comment);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //ACT:
        List<Comment> commentsList = commentRepository.findAllByRequestId(savedRequest.getId());

        //ASSERT:
        assertNotNull(commentsList);
        assertEquals(2, commentsList.size());
    }

    //custom query -> List<Comment> findAllByRequestIdAndType(Long requestId, ReviewType reviewType);
    @Test
    @DisplayName("Find All Comments By Request Id and Type Method")
    public void findAllCommentsByRequestIdAndTypeTest(){
        //ARRANGE:
        //creating another comment
        Date date2025 = java.sql.Date.valueOf(LocalDate.of(2025,10,28));
        Comment comment2 = Comment.builder()
                .content("can not proceed")
                .authorizedBy("DEAN")
                .type(ReviewType.ADMIN_DIV)
                .request(savedRequest)
                .createdBy(savedCreatedByUser)
                .createdDate(date2025)
                .build();

        //creating another comment with another request
        //a new request
        Request request2 = Request.builder()
                .title("Keyboards")
                .status(RequestStatus.PENDING_PROCUREMENT)
                .createdBy(savedCreatedByUser)
                .subdivList(List.of(savedSubdivForRequest))
                .admindiv(savedAdmindivForRequest)
                .createdDate(new Date())
                .build();
        Request savedRequest2 = requestRepository.save(request2);

        //comment with another request
        Comment comment3 = Comment.builder()
                .content("can not proceed")
                .authorizedBy("DEAN")
                .type(ReviewType.ADMIN_DIV)
                .request(savedRequest2)
                .createdBy(savedCreatedByUser)
                .createdDate(date2025)
                .build();

        //saving all comments
        commentRepository.save(comment);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //ACT:
        List<Comment> commentsList = commentRepository.findAllByRequestIdAndType(savedRequest.getId(), ReviewType.ADMIN_DIV);

        //ASSERT:
        assertNotNull(commentsList);
        assertEquals(1, commentsList.size());
    }

}