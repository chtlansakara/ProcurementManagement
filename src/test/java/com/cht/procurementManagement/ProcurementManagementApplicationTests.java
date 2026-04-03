package com.cht.procurementManagement;

import com.cht.procurementManagement.dto.procurement.ProcurementCreateDto;
import com.cht.procurementManagement.dto.procurement.ProcurementResponseDto;
import com.cht.procurementManagement.dto.procurement.ProcurementStatusUpdateDto;
import com.cht.procurementManagement.entities.*;
import com.cht.procurementManagement.enums.ProcurementStage;
import com.cht.procurementManagement.enums.RequestStatus;
import com.cht.procurementManagement.enums.UserRole;
import com.cht.procurementManagement.repositories.*;
import com.cht.procurementManagement.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Integration Tests")
class ProcurementManagementApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	//objects required
	private ProcurementCreateDto procurementCreateDto;

	//dto to update
	private ProcurementCreateDto procurementUpdateDto;

	private ProcurementStatusUpdateDto procurementStatusUpdateDto;

	//repositories required to created required objects
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DesignationRepository designationRepository;
	@Autowired
	private AdmindivRepository admindivRepository;

	@Autowired
	private SubdivRepository subdivRepository;

	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private ProcurementSourceRepository procurementSourceRepository;

	@Autowired
	private ProcurementStatusRepository procurementStatusRepository;

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private ProcurementRepository procurementRepository;

	@Autowired
	private ProcurementStatusUpdateRepository procurementStatusUpdateRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	//for JWT token (to send with each request)
	@Autowired
	private JwtUtil jwtutil;

	private String token;


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
		Designation designationForUser = designationRepository.save(Designation.builder()
				.title("Supplies Clerk")
				.code("SF")
				.build());


		Designation designationForAdmindiv = designationRepository.save( Designation.builder()
				.title("Assitant Registrar")
				.code("AR")
				.build());

		Admindiv admindivForUser = admindivRepository.save( Admindiv.builder()
				.name("Administration")
				.code("ADMIN")
				.responsibleDesignation(designationForAdmindiv)
				.build());

		Admindiv admindivForRequest = admindivRepository.save(Admindiv.builder()
				.name("Faculty of Science")
				.code("FOS")
				.responsibleDesignation(designationForAdmindiv)
				.build());

		Subdiv subdivForUser= subdivRepository.save( Subdiv.builder()
				.name("Supplies Division")
				.code("SUP-ADMIN")
				.admindiv(admindivForUser)
				.build());


		Subdiv subdivForRequest = subdivRepository.save( Subdiv.builder()
				.name("Department of Biology")
				.code("FOS-DOB")
				.admindiv(admindivForRequest)
				.build());


		//needed for mock User as well
		User savedUser = userRepository.save( User.builder()
				.name("Sarath")
				.email("sarath@gmail.com")
				.password(passwordEncoder.encode("sarath"))
				.userRole(UserRole.SUPPLIESUSER)
				.designation(designationForUser)
				.subdiv(subdivForUser)
				.admindiv(admindivForUser)
				.build());

		//generate a token
		token = jwtutil.generateToken(savedUser);

		ProcurementSource savedProcurementSource = procurementSourceRepository.save( ProcurementSource.builder()
				.name("102")
				.description("Government funding")
				.build());


		Request savedRequest = requestRepository.save(Request.builder()
				.title("Keyboards")
				.status(RequestStatus.PENDING_PROCUREMENT)
				.createdBy(savedUser)
				.subdivList(List.of(subdivForRequest))
				.admindiv(admindivForRequest)
				.createdDate(new Date())
				.build());

		ProcurementStatus savedProcurementStatus = procurementStatusRepository.save(ProcurementStatus.builder()
				.name("Creating tender documents")
				.build());

		Vendor savedVendor = vendorRepository.save(Vendor.builder()
						.name("Abans PVT LTD")
						.comments("Worked with in 2021")
						.registeredDate(new Date())
						.build());



		procurementCreateDto = ProcurementCreateDto.builder()
				.name("Keyboards")
				.quantity(10L)
				.estimatedAmount(BigDecimal.valueOf(10000))
				.assignedToUserId(savedUser.getId())
				.requestId(savedRequest.getId())
				.sourceId(savedProcurementSource.getId())
				.build();

		//adding the vendor
		procurementUpdateDto = ProcurementCreateDto.builder()
				//changing the name
				.name("Computer Keyboards")
				.quantity(10L)
				.estimatedAmount(BigDecimal.valueOf(10000))
				.assignedToUserId(savedUser.getId())
				.requestId(savedRequest.getId())
				.sourceId(savedProcurementSource.getId())
				//adding a vendor
				.vendorId(savedVendor.getId())
				.build();

		//update status
		procurementStatusUpdateDto = ProcurementStatusUpdateDto.builder()
				.comment("Delayed")
				.procurementStage(ProcurementStage.PURCHASE_PROCESS_COMMENCED.toString())
				.procurementStatusId(savedProcurementStatus.getId())
				.procurementAssignedToUserId(savedUser.getId())
				.build();



	}

	//clean up created required objects
	@AfterEach
	void cleanup(){
		procurementStatusUpdateRepository.deleteAll();
		procurementRepository.deleteAll();
		procurementSourceRepository.deleteAll();
		vendorRepository.deleteAll();
		procurementStatusRepository.deleteAll();
		requestRepository.deleteAll();
		userRepository.deleteAll();
		subdivRepository.deleteAll();
		admindivRepository.deleteAll();
		designationRepository.deleteAll();

	}


	@Nested
	@DisplayName("Procurement Integration Tests")
	class procurementTests{
		@Test
		@DisplayName("Create Procurement - Successfully")
		public void createProcurement_integrationTest() throws Exception {
			//ARRANGE:
			// request body object - created in @BeforeEach

			//ACT:
			ResultActions response = mockMvc.perform(MockMvcRequestBuilders
					.post("/api/supplies/procurement")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementCreateDto))
					.accept("application/json"));


			//ASSERT:
			response.andExpect(status().isCreated())
					.andExpect(jsonPath("$.id").exists())
					.andExpect(jsonPath("$.name").value(procurementCreateDto.getName()));

			List<Procurement> saved = procurementRepository.findAll();
			assertThat(saved).hasSize(1);
			assertThat(saved.get(0).getName()).isEqualTo("Keyboards");
			assertThat(saved.get(0).getQuantity()).isEqualTo(10L);

		}
		@Test
		@DisplayName("Create Procurement - Throws EntityNotFound Exception")
		public void createProcurement_throwsStatus404Exception() throws Exception {
			//ARRANGE:
			//request set is non existent
			procurementCreateDto.setRequestId(32L);

			//ACT:
			ResultActions response = mockMvc.perform(MockMvcRequestBuilders
					.post("/api/supplies/procurement")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementCreateDto))
					.accept("application/json"));

			//ASSERT:
			response.andExpect(status().isNotFound())
					.andExpect(content().string("Request selected is not found"));

		}

		@Test
		@DisplayName("Create Procurement - Throws RuntimeException")
		public void createProcurement_throwsStatus400Exception() throws Exception {
			//ARRANGE:
			//set request id as null
			procurementCreateDto.setRequestId(null);

			//ACT:
			ResultActions response = mockMvc.perform(MockMvcRequestBuilders
					.post("/api/supplies/procurement")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementCreateDto))
					.accept("application/json"));

			//ASSERT:
			response.andExpect(status().isBadRequest())
					.andExpect(content().string("Request is empty"));

		}


		@Test
		@DisplayName("Update Procurement - Successfully")
		public void updateProcurement_integrationTest() throws Exception {
			//ARRANGE:
			// request body object - created in @BeforeEach
			//create first (with create dto)
			 ResultActions responseCreated = mockMvc.perform(MockMvcRequestBuilders
					.post("/api/supplies/procurement")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementCreateDto))
					.accept("application/json"));

			 //extract the id
			String responseBodyReturned = responseCreated.andReturn().getResponse().getContentAsString();
			long procurementId = objectMapper.readTree(responseBodyReturned).get("id").asLong();

			//ACT:
			//(with update dto)
			ResultActions response = mockMvc.perform(MockMvcRequestBuilders
					.put("/api/supplies/procurement/"+procurementId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementUpdateDto))
					.accept("application/json"));


			//ASSERT:
			response.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").exists())
					.andExpect(jsonPath("$.name").value(procurementUpdateDto.getName()))
					.andExpect(jsonPath("$.vendorId").exists());

		}

		@Test
		@DisplayName("Update Procurement - Throws Exception")
		public void updateProcurement_throwsException() throws Exception {
			//ARRANGE:
			//non-existent procurement Id
			Long procurementId = 20L;

			//ACT:
			//(with update dto)
			ResultActions response = mockMvc.perform(MockMvcRequestBuilders
					.put("/api/supplies/procurement/"+procurementId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementUpdateDto))
					.accept("application/json"));


			//ASSERT:
			response.andExpect(status().isNotFound())
					.andExpect(content().string("Procurement not found"));

		}

		@Test
		@DisplayName("Get All Procurement - Successfully")
		public void getAllProcurement_integrationTest() throws Exception {
			//create first
			mockMvc.perform(MockMvcRequestBuilders
					.post("/api/supplies/procurement")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementCreateDto))
					.accept("application/json"));

			//check
			mockMvc.perform(MockMvcRequestBuilders.get("/api/supplies/procurement")
							.header("Authorization", "Bearer "+token))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$").isArray())  //is a list
					.andExpect(jsonPath("$.length()").value(1))
					.andExpect(jsonPath("$.[0].id").exists())
					.andExpect(jsonPath("$[0].name").value(procurementCreateDto.getName()));

		}

		@Test
		@DisplayName("Get Procurement By Id - Successfully")
		public void getProcurementById_integrationTest() throws Exception {
			//create first
			ResultActions responseCreated =  mockMvc.perform(MockMvcRequestBuilders
					.post("/api/supplies/procurement")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementCreateDto))
					.accept("application/json"));

			//extract the id
			String responseBodyReturned = responseCreated.andReturn().getResponse().getContentAsString();
			long procurementId = objectMapper.readTree(responseBodyReturned).get("id").asLong();

			//check
			mockMvc.perform(MockMvcRequestBuilders.get("/api/supplies/procurement/"+ procurementId)
							.header("Authorization", "Bearer "+token))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").exists())  //is a list
					.andExpect(jsonPath("$.name").value(procurementCreateDto.getName()));

		}
		@Test
		@DisplayName("Delete Procurement - Successfully")
		public void deleteProcurement_integrationTest() throws Exception {
			//create first
			ResultActions responseCreated =  mockMvc.perform(MockMvcRequestBuilders
					.post("/api/supplies/procurement")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementCreateDto))
					.accept("application/json"));

			//extract the id
			String responseBodyReturned = responseCreated.andReturn().getResponse().getContentAsString();
			long procurementId = objectMapper.readTree(responseBodyReturned).get("id").asLong();

			//check
			mockMvc.perform(MockMvcRequestBuilders.delete("/api/supplies/procurement/"+ procurementId)
							.header("Authorization", "Bearer "+token))
					.andExpect(status().isOk())
					.andExpect(content().string(""));

			assertFalse(procurementRepository.existsById(procurementId));

		}


		@Test
		@DisplayName("Update Status - Successfully")
		public void updateStatus_integrationTest() throws Exception {
			//ARRANGE:
			// request body object - created in @BeforeEach
			//create first (with create dto)
			ResultActions responseCreated = mockMvc.perform(MockMvcRequestBuilders
					.post("/api/supplies/procurement")
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementCreateDto))
					.accept("application/json"));

			//extract the id
			String responseBodyReturned = responseCreated.andReturn().getResponse().getContentAsString();
			long procurementId = objectMapper.readTree(responseBodyReturned).get("id").asLong();

			//ACT:
			//(with update dto)
			ResultActions response = mockMvc.perform(MockMvcRequestBuilders
					.put("/api/supplies/procurement/status/"+procurementId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementStatusUpdateDto))
					.accept("application/json"));


			//ASSERT:
			response.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").exists())
					.andExpect(jsonPath("$.comment").value(procurementStatusUpdateDto.getComment()))
					.andExpect(jsonPath("$.procurementId").value(procurementId))
					.andExpect(jsonPath("$.createdOn").exists());

		}


		@Test
		@DisplayName("Update Status - Throws Exception")
		public void updateStatus_throwsException() throws Exception {
			//ARRANGE:
			//non-existent procurement Id
			Long procurementId = 20L;

			//ACT:
			//(with update dto)
			ResultActions response = mockMvc.perform(MockMvcRequestBuilders
					.put("/api/supplies/procurement/status/"+procurementId)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+token)
					.content(objectMapper.writeValueAsString(procurementStatusUpdateDto))
					.accept("application/json"));


			//ASSERT:
			response.andExpect(status().isNotFound())
					.andExpect(content().string("Procurement not found"));

		}


	}



}
