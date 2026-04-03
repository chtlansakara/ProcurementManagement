package com.cht.procurementManagement.controllers.supplies;

import com.cht.procurementManagement.config.JwtAuthenticationFilter;
import com.cht.procurementManagement.config.WebSecurityConfig;
import com.cht.procurementManagement.dto.procurement.ProcurementCreateDto;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.entities.*;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.UserRepository;
import com.cht.procurementManagement.services.AuditLog.AuditLogService;
import com.cht.procurementManagement.services.jwt.UserService;
import com.cht.procurementManagement.services.procurement.ProcurementService;
import com.cht.procurementManagement.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ProcurementController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Procurement Controller - Unit Tests")
class ProcurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //controller related dependencies
    @MockitoBean
    private ProcurementService procurementService;

    @MockitoBean
    private AuditLogService auditLogService;

    //mock security  related dependencies
    @MockitoBean
    private WebSecurityConfig webSecurityConfig;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private UserService userService;

    private ObjectMapper objectMapper;

    //objects required
    private ProcurementCreateDto procurementCreateDto;

    private ProcurementResponseDto procurementResponseDto;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
        //ARRANGE:
        Designation designationForUser = Designation.builder()
                .title("Supplies Clerk")
                .code("SF")
                .build();


        Designation designationForAdmindiv = Designation.builder()
                .title("Assitant Registrar")
                .code("AR")
                .build();

        Admindiv admindivForUser = Admindiv.builder()
                .name("Administration")
                .code("ADMIN")
                .responsibleDesignation(designationForAdmindiv)
                .build();

        Admindiv admindivForRequest = Admindiv.builder()
                .name("Faculty of Science")
                .code("FOS")
                .responsibleDesignation(designationForAdmindiv)
                .build();

        Subdiv subdivForUser= Subdiv.builder()
                .name("Supplies Division")
                .code("SUP-ADMIN")
                .admindiv(admindivForUser)
                .build();


        Subdiv subdivForRequest = Subdiv.builder()
                .name("Department of Biology")
                .code("FOS-DOB")
                .admindiv(admindivForRequest)
                .build();


        User createdByUser = User.builder()
                .name("Sarath")
                .email("sarath@gmail.com")
                .userRole(UserRole.SUPPLIESUSER)
                .designation(designationForUser)
                .subdiv(subdivForUser)
                .admindiv(admindivForUser)
                .build();


        Request request = Request.builder()
                .title("Keyboards")
                .status(RequestStatus.PENDING_PROCUREMENT)
                .createdBy(createdByUser)
                .subdivList(List.of(subdivForRequest))
                .admindiv(admindivForRequest)
                .createdDate(new Date())
                .build();



        procurementCreateDto = ProcurementCreateDto.builder()
                .name("Keyboards")
                .quantity(10L)
                .estimatedAmount(BigDecimal.valueOf(10000))
                .assignedToUserId(1L)
                .requestId(1L)
                .statusId(1L)
                .vendorId(1L)
                .sourceId(1L)
                .build();

        procurementResponseDto = ProcurementResponseDto.builder()
                .name("Keyboards")
                .quantity(10L)
                .estimatedAmount(BigDecimal.valueOf(10000))
                .assignedToUserId(1L)
                .requestId(1L)
                .statusId(1L)
                .vendorId(1L)
                .sourceId(1L)
                .build();

    }

    @Test
    @DisplayName("Create Procurement - Successfully, Returns Procurement Response Dto")
    public void createProcurement_Success_ReturnsResponseDto() throws Exception {

        //ARRANGE:
        //mock service call
        given(procurementService.createProcurement(ArgumentMatchers.any()))
                .willReturn(procurementResponseDto);

        //ACT:
        ResultActions response = mockMvc.perform(post("/api/supplies/procurement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(procurementCreateDto)));

        //ASSERT:
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                        CoreMatchers.is(procurementResponseDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Create Procurement - Returns 400 Bad Request")
    public void createProcurement_ReturnsBadRequest() throws Exception {

        //ARRANGE:
        //mock service call to return null
        given(procurementService.createProcurement(ArgumentMatchers.any()))
                .willReturn(null);

        //ACT:
        ResultActions response = mockMvc.perform(post("/api/supplies/procurement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(procurementCreateDto)));

        //ASSERT:
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Procurement couldn't be created."));
    }

//    @Test
//    @DisplayName("Create Procurement - Throws Exception & Returns 500 Bad Request")
//    public void createProcurement_ThrowsException() throws Exception {
//
//        //ARRANGE:
//        //mock service call to return null
//        given(procurementService.createProcurement(ArgumentMatchers.any()))
//                .willThrow(new RuntimeException("Unexpected error occurred"));
//
//        //ACT:
//        ResultActions response = mockMvc.perform(post("/api/supplies/procurement")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(procurementCreateDto)));
//
//        //ASSERT:
//        response.andExpect(MockMvcResultMatchers.status().isInternalServerError())
//                .andExpect(MockMvcResultMatchers.content()
//                        .string("Unexpected error occurred"));
//
//    }

    @Test
    @DisplayName("Get All Procurement - Successfully, Returns List of Procurement Response Dto")
    public void getAllProcurement_Success_ReturnsListOfResponseDto() throws Exception {

        //ARRANGE:
        //mock service call
        given(procurementService.getProcurement())
                .willReturn(List.of(procurementResponseDto));

        //ACT:
        ResultActions response = mockMvc.perform(get("/api/supplies/procurement")
                .contentType(MediaType.APPLICATION_JSON));
        //ASSERT:
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1));

//                .param("parameterName", "stringValue" )
//                .param("secondParameterName", "stringValue");
    }


    @Test
    @DisplayName("Get Procurement By Id - Successfully, Returns Procurement Response Dto")
    public void getProcurementById_Success_ReturnsResponseDto() throws Exception {

        //ARRANGE:
        Long procurementId = 1L;

        //mock service call
        given(procurementService.getProcurementById(procurementId))
                .willReturn(procurementResponseDto);

        //ACT:
        ResultActions response = mockMvc.perform(get("/api/supplies/procurement/"+procurementId)
                .contentType(MediaType.APPLICATION_JSON));

        //ASSERT:
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                        CoreMatchers.is(procurementResponseDto.getName())));

    }

    @Test
    @DisplayName("Update Procurement - Successfully, Returns Procurement Response Dto")
    public void updateProcurement_Success_ReturnsResponseDto() throws Exception {

        //ARRANGE:
        Long procurementId = 1L;

        //mock service call
        given(procurementService.updateProcurement(
                ArgumentMatchers.eq(procurementId),
                ArgumentMatchers.any(ProcurementCreateDto.class)))
                .willReturn(procurementResponseDto);

        //ACT:
        ResultActions response = mockMvc.perform(put("/api/supplies/procurement/"+procurementId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(procurementCreateDto)));

        //ASSERT:
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                        CoreMatchers.is(procurementResponseDto.getName())));

    }

    @Test
    @DisplayName("Delete Procurement - Successfully")
    public void deleteProcurement_Success() throws Exception {

        //ARRANGE:
        Long procurementId = 1L;

        //mock service call
        doNothing().when(procurementService).deleteProcurement(procurementId);

        //ACT:
        ResultActions response = mockMvc.perform(delete("/api/supplies/procurement/"+procurementId)
                .contentType(MediaType.APPLICATION_JSON)
               );

        //ASSERT:
        response.andExpect(MockMvcResultMatchers.status().isOk());

    }



}