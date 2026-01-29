package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.api.security.util.JwtProvider;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.payments.Method;
import br.edu.ifpb.hopin_daw2.core.domain.payments.Payment;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;
import br.edu.ifpb.hopin_daw2.core.service.TripService;
import br.edu.ifpb.hopin_daw2.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@WebMvcTest(TripController.class)
@AutoConfigureMockMvc
public class TripControllerUTTests {

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TripService tripService;

    private static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    @Test
    @WithMockUser(username = "cliente@teste.com", roles = "CUSTOMER")
    void deveCriarUmaViagemComSucesso() throws Exception {
        UUID driverId = UUID.randomUUID();
        UUID tripId = UUID.randomUUID();

        TripRequestDTO request = new TripRequestDTO(
                driverId,
                Method.CREDIT_CARD,
                BigDecimal.valueOf(78.50),
                "Avenida Paulista, São Paulo",
                "Aeroporto de Guarulhos"
        );

        Customer mockCustomer = new Customer();
        mockCustomer.setId(UUID.randomUUID());
        mockCustomer.setName("Maria Silva");
        mockCustomer.setEmail("cliente@teste.com");

        Driver mockDriver = new Driver();
        mockDriver.setId(driverId);
        mockDriver.setName("João Motorista");

        Payment mockPayment = new Payment();
        mockPayment.setId(UUID.randomUUID());
        mockPayment.setMethod(Method.CREDIT_CARD);
        mockPayment.setAmount(request.paymentAmount());
        mockPayment.setCreatedAt(LocalDateTime.now());

        TripResponseDTO response = new TripResponseDTO(
                tripId,
                mockCustomer,
                mockDriver,
                mockPayment,
                Status.REQUESTED,
                request.origin(),
                request.destination(),
                LocalDateTime.now()
        );

        when(tripService.createTrip(any(TripRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/trip")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(tripId.toString()))
                .andExpect(jsonPath("$.origin").value(request.origin()))
                .andExpect(jsonPath("$.destination").value(request.destination()))
                .andExpect(jsonPath("$.status").value("REQUESTED"))
                .andExpect(jsonPath("$.payment.amount").value("78.5"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @WithMockUser(username = "cliente@teste.com", roles = "CUSTOMER")
    void deveRetornarUmaViagemPorIdComSucesso() throws Exception {
        UUID tripId = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setEmail("cliente@teste.com");

        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setMethod(Method.PIX);
        payment.setAmount(BigDecimal.valueOf(45.00));

        TripResponseDTO dto = new TripResponseDTO(
                tripId,
                customer,
                driver,
                payment,
                Status.ACCEPTED,
                "Shopping Center Norte",
                "Rua Augusta, 500",
                LocalDateTime.now().minusHours(2)
        );

        when(tripService.getTripById(tripId)).thenReturn(dto);

        mockMvc.perform(get("/trip/{tripId}", tripId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tripId.toString()))
                .andExpect(jsonPath("$.origin").value(dto.origin()))
                .andExpect(jsonPath("$.destination").value(dto.destination()))
                .andExpect(jsonPath("$.status").value("ACCEPTED"))
                .andExpect(jsonPath("$.payment.amount").value("45.0"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    @WithMockUser(username = "motorista@teste.com", roles = "DRIVER")
    void deveAceitarUmaViagemComSucesso() throws Exception {
        UUID tripId = UUID.randomUUID();

        TripResponseDTO response = criarTripResponseMock(tripId, Status.ACCEPTED);

        when(tripService.editTripStatus(tripId, Status.ACCEPTED)).thenReturn(response);

        mockMvc.perform(patch("/trip/{tripId}/accept", tripId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tripId.toString()))
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    @WithMockUser(username = "cliente@teste.com", roles = "CUSTOMER")
    void deveCancelarUmaViagemComSucesso() throws Exception {
        UUID tripId = UUID.randomUUID();

        TripResponseDTO response = criarTripResponseMock(tripId, Status.CANCELLED);

        when(tripService.editTripStatus(tripId, Status.CANCELLED)).thenReturn(response);

        mockMvc.perform(patch("/trip/{tripId}/cancel", tripId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tripId.toString()))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @WithMockUser(username = "motorista@teste.com", roles = "DRIVER")
    void deveIniciarUmaViagemComSucesso() throws Exception {
        UUID tripId = UUID.randomUUID();

        TripResponseDTO response = criarTripResponseMock(tripId, Status.STARTED);

        when(tripService.editTripStatus(tripId, Status.STARTED)).thenReturn(response);

        mockMvc.perform(patch("/trip/{tripId}/start", tripId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tripId.toString()))
                .andExpect(jsonPath("$.status").value("STARTED"));
    }

    @Test
    @WithMockUser(username = "motorista@teste.com", roles = "DRIVER")
    void deveCompletarUmaViagemComSucesso() throws Exception {
        UUID tripId = UUID.randomUUID();

        TripResponseDTO response = criarTripResponseMock(tripId, Status.COMPLETED);

        when(tripService.editTripStatus(tripId, Status.COMPLETED)).thenReturn(response);

        mockMvc.perform(patch("/trip/{tripId}/complete", tripId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tripId.toString()))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    private TripResponseDTO criarTripResponseMock(UUID tripId, Status status) {
        Customer c = new Customer();
        c.setId(UUID.randomUUID());
        c.setName("Ana");
        c.setEmail("cliente@teste.com");

        Driver d = new Driver();
        d.setId(UUID.randomUUID());
        d.setName("Pedro");

        Payment p = new Payment();
        p.setId(UUID.randomUUID());
        p.setMethod(Method.CASH);
        p.setAmount(BigDecimal.valueOf(32.90));

        return new TripResponseDTO(
                tripId,
                c,
                d,
                p,
                status,
                "Praça da Sé",
                "Itaim Bibi",
                LocalDateTime.now()
        );
    }
}