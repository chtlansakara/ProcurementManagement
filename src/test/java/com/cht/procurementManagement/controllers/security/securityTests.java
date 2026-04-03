package com.cht.procurementManagement.controllers.security;

import com.cht.procurementManagement.controllers.supplies.ProcurementController;
import com.cht.procurementManagement.dto.AuthenticationRequest;
import com.cht.procurementManagement.dto.procurement.ProcurementStatusUpdateDto;
import com.cht.procurementManagement.entities.Admindiv;
import com.cht.procurementManagement.entities.Designation;
import com.cht.procurementManagement.entities.Subdiv;
import com.cht.procurementManagement.entities.User;
import com.cht.procurementManagement.enums.ProcurementStage;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.*;
import com.cht.procurementManagement.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Security Integration Tests")
public class securityTests {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    //repositories required to User
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DesignationRepository designationRepository;
    @Autowired
    private AdmindivRepository admindivRepository;
    @Autowired
    private SubdivRepository subdivRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //for JWT token (to send with each request)
    @Autowired
    private JwtUtil jwtutil;

    //tokens for each user-role
    private String adminUserToken;
    private String suppliesUserToken;
    private String admindivUserToken;
    private String subdivUserToken;

    //DTOs required
    private AuthenticationRequest validLoginRequest;
    private AuthenticationRequest invalidLoginRequest;

    //Saved objects required
    private Designation savedDesignation;
    private Subdiv savedSubdiv;
    private Admindiv savedAdmindiv;


    //container
    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0");

    @DynamicPropertySource
    static void configureContainerProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();


        //create and save required objects:
        savedDesignation = designationRepository.save(Designation.builder()
                .title("Supplies Clerk")
                .code("SF")
                .build());

        Designation designationForAdmindiv = designationRepository.save( Designation.builder()
                .title("Assitant Registrar")
                .code("AR")
                .build());

        savedAdmindiv = admindivRepository.save( Admindiv.builder()
                .name("Administration")
                .code("ADMIN")
                .responsibleDesignation(designationForAdmindiv)
                .build());

        savedSubdiv = subdivRepository.save( Subdiv.builder()
                .name("Supplies Division")
                .code("SUP-ADMIN")
                .admindiv(savedAdmindiv)
                .build());


        //User per user-role
        User adminUser = userRepository.save( User.builder()
                .name("admintest")
                .email("admintest@test.com")
                .password(passwordEncoder.encode("admin"))
                .userRole(UserRole.ADMIN)
                .designation(savedDesignation)
                .subdiv(savedSubdiv)
                .admindiv(savedAdmindiv)
                .build());

        //generate a token
        adminUserToken = jwtutil.generateToken(adminUser);

        User suppliesUser = userRepository.save( User.builder()
                .name("supplies")
                .email("supplies@test.com")
                .password(passwordEncoder.encode("supplies"))
                .userRole(UserRole.SUPPLIESUSER)
                .designation(savedDesignation)
                .subdiv(savedSubdiv)
                .admindiv(savedAdmindiv)
                .build());

        //generate a token
        suppliesUserToken = jwtutil.generateToken(suppliesUser);

        User admindivUser = userRepository.save( User.builder()
                .name("admindiv")
                .email("admindiv@test.com")
                .password(passwordEncoder.encode("admindiv"))
                .userRole(UserRole.ADMINDIVUSER)
                .designation(savedDesignation)
                .subdiv(savedSubdiv)
                .admindiv(savedAdmindiv)
                .build());

        //generate a token
        admindivUserToken = jwtutil.generateToken(admindivUser);

        User subdivUser = userRepository.save( User.builder()
                .name("subdiv")
                .email("subdiv@test.com")
                .password(passwordEncoder.encode("subdiv"))
                .userRole(UserRole.SUBDIVUSER)
                .designation(savedDesignation)
                .subdiv(savedSubdiv)
                .admindiv(savedAdmindiv)
                .build());

        //generate a token
        subdivUserToken = jwtutil.generateToken(subdivUser);

        //Login Request Dto
        validLoginRequest = AuthenticationRequest.builder()
                .email("supplies@test.com")
                .password("supplies")
                .build();
        invalidLoginRequest = AuthenticationRequest.builder()
                .email("supplies@test.com")
                .password("wrongPassword")
                .build();

    }

    @AfterEach
    void cleanup(){
        userRepository.deleteAll();
        subdivRepository.deleteAll();
        admindivRepository.deleteAll();
        designationRepository.deleteAll();
    }


    @Nested
    @DisplayName("No Token Tests")
    class noToken_tests{
        @Test
        @DisplayName("Access ADMIN Endpoints - Returns Unauthorized (status 401)")
        public void adminEndpoint_returnsUnauthorized() throws Exception{
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users"))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        @DisplayName("Access SUPPLIES Endpoints - Returns Unauthorized (status 401)")
        public void suppliesEndpoint_returnsUnauthorized() throws Exception{
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/supplies/requests"))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        @DisplayName("Access ADMINDIV Endpoints - Returns Unauthorized (status 401)")
        public void admindivEndpoint_returnsUnauthorized() throws Exception{
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admindiv/requests"))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        @DisplayName("Access SUBDIV Endpoints - Returns Unauthorized (status 401)")
        public void subdivEndpoint_returnsUnauthorized() throws Exception{
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/subdiv/requests"))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        @DisplayName("Notifications Endpoint - Returns Unauthorized (status 401)")
        void notificationEndpoint_returnsUnauthorized() throws Exception{
            mockMvc.perform(MockMvcRequestBuilders.get("/api/notifications"))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

    }








        @Nested
        @DisplayName("Authentication Endpoint - No Token")
        class auth_tests{
            @Test
            @DisplayName("Login- Successfully Returns a Token for Valid User")
            public void testLogin_returnsAToken() throws Exception{
                ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)));

                //ASSERT:
                response.andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.jwt").exists())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(validLoginRequest.getEmail()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.userRole").value(UserRole.SUPPLIESUSER.name()));

            }

            @Test
            @DisplayName("Login - Returns Bad Request (status 400) When Wrong Password for Valid User")
            public void testLogin_returns400WhenInvalidAuthenticationRequest() throws Exception{
                ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoginRequest)));

                //ASSERT:
                response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.content()
                                .string("Incorrect password or username!"));
            }

            @Test
            @DisplayName("Login - Returns Bad Request (status 400) When User doesn't exist")
            public void testLogin_returns400WhenUserDoesNotExist() throws Exception{
                //Non-existent user
                AuthenticationRequest nonExistentLoginRequest = AuthenticationRequest.builder()
                        .email("nosuchuser@test.com")
                        .password("password")
                        .build();

                ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentLoginRequest)));

                //ASSERT:
                response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.content()
                                .string("Incorrect password or username!"));
            }


        }


    @Nested
    @DisplayName("Invalid Token Tests")
    class invalidToken_tests {
        @Test
        @DisplayName("Tampered Token - Returns Unauthorized (status 401)")
        public void tamperedToken_returnsUnauthorized() throws Exception {
            //change the token
            String tamperedAdminToken = adminUserToken+"21";

            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users")
                            .header("Authorization", "Bearer " +tamperedAdminToken))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        @DisplayName("Empty Token - Returns Unauthorized (status 401)")
        public void emptyToken_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users")
                            .header("Authorization", "Bearer "))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

    }

    @Nested
    @DisplayName("Role-based Access Tests")
    class roleBasedAccess_tests {

        //ADMIN ENDPOINT
        @Test
        @DisplayName("ADMIN endpoint - by ADMIN User - Successfully Accessed ")
        public void adminEndpoint_accessedByADMINSuccessfully() throws Exception {

            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users")
                            .header("Authorization", "Bearer " + adminUserToken))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("ADMIN endpoint - by SUPPLIES User -Returns Forbidden (status 403)")
        public void adminEndpoint_accessedBySUPPLIES_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users")
                            .header("Authorization", "Bearer " + suppliesUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        @DisplayName("ADMIN endpoint - by ADMINDIV User -Returns Forbidden (status 403)")
        public void adminEndpoint_accessedByADMINDIV_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users")
                            .header("Authorization", "Bearer " + admindivUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        @DisplayName("ADMIN endpoint - by SUBDIV User -Returns Forbidden (status 403)")
        public void adminEndpoint_accessedBySUBDIV_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users")
                            .header("Authorization", "Bearer " + subdivUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }


        //SUPPLIES ENDPOINT
        @Test
        @DisplayName("SUPPLIES endpoint - by SUPPLIES User - Successfully Accessed ")
        public void suppliesEndpoint_accessedBySUPPLIESSuccessfully() throws Exception {

            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/supplies/requests")
                            .header("Authorization", "Bearer " + suppliesUserToken))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("SUPPLIES endpoint - by ADMIN User -Returns Forbidden (status 403)")
        public void suppliesEndpoint_accessedBySUPPLIES_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/supplies/requests")
                            .header("Authorization", "Bearer " + adminUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        @DisplayName("SUPPLIES endpoint - by ADMINDIV User -Returns Forbidden (status 403)")
        public void suppliesEndpoint_accessedBySUPPLIESDIV_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/supplies/requests")
                            .header("Authorization", "Bearer " + admindivUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        @DisplayName("SUPPLIES endpoint - by SUBDIV User -Returns Forbidden (status 403)")
        public void suppliesEndpoint_accessedBySUBDIV_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/supplies/requests")
                            .header("Authorization", "Bearer " + subdivUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }


        //ADMINDIV ENDPOINT
        @Test
        @DisplayName("ADMINDIV endpoint - by ADMINDIV User - Successfully Accessed ")
        public void admindivEndpoint_accessedByADMINDIVSuccessfully() throws Exception {

            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admindiv/requests")
                            .header("Authorization", "Bearer " + admindivUserToken))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("ADMINDIV endpoint - by ADMIN User -Returns Forbidden (status 403)")
        public void admindivEndpoint_accessedByADMIN_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admindiv/requests")
                            .header("Authorization", "Bearer " + adminUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        @DisplayName("ADMINDIV endpoint - by SUPPLIES User -Returns Forbidden (status 403)")
        public void admindivEndpoint_accessedBySUPPLIES_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admindiv/requests")
                            .header("Authorization", "Bearer " + suppliesUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        @DisplayName("ADMINDIV endpoint - by SUBDIV User -Returns Forbidden (status 403)")
        public void admindivEndpoint_accessedBySUBDIV_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/admindiv/requests")
                            .header("Authorization", "Bearer " + subdivUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }


        //SUBDIV ENDPOINT
        @Test
        @DisplayName("SUBDIV endpoint - by SUBDIV User - Successfully Accessed ")
        public void subdivEndpoint_accessedBySUBDIVSuccessfully() throws Exception {

            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/subdiv/requests")
                            .header("Authorization", "Bearer " + subdivUserToken))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("SUBDIV endpoint - by ADMIN User -Returns Forbidden (status 403)")
        public void subdivEndpoint_accessedByADMIN_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/subdiv/requests")
                            .header("Authorization", "Bearer " + adminUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        @DisplayName("SUBDIV endpoint - by SUPPLIES User -Returns Forbidden (status 403)")
        public void subdivEndpoint_accessedBySUPPLIES_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/subdiv/requests")
                            .header("Authorization", "Bearer " + suppliesUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        @DisplayName("SUBDIV endpoint - by ADMINDIV User -Returns Forbidden (status 403)")
        public void subdivEndpoint_accessedByADMINDIV_returnsUnauthorized() throws Exception {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/subdiv/requests")
                            .header("Authorization", "Bearer " + admindivUserToken))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        //NOTIFICATION ENDPOINT
        @Test
        @DisplayName("Notifications endpoint - by all User Roles - Successfully Accessed")
        void notificationEndpoint_accessedByAllUsersSuccessfully() throws Exception{
            //tokens
            String[] tokens = {adminUserToken, suppliesUserToken, admindivUserToken, subdivUserToken};
            for(String token : tokens){
                mockMvc.perform(MockMvcRequestBuilders.get("/api/notifications")
                                .header("Authorization", "Bearer " + token))
                        .andExpect(MockMvcResultMatchers.status().isOk());
            }

        }




    }
}
