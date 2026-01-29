package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.CustomerRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.CustomerResponseDTO;
import br.edu.ifpb.hopin_daw2.api.dto.TripResponseDTO;
import br.edu.ifpb.hopin_daw2.api.security.util.JwtProvider;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;
import br.edu.ifpb.hopin_daw2.core.service.CustomerService;
import br.edu.ifpb.hopin_daw2.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({CustomerController.class})
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerUTTests {

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @Test
    void deveRetornarClientePorIdComSucesso() throws Exception {
        // Arrange
        UUID customerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        CustomerResponseDTO response = new CustomerResponseDTO(
                customerId,
                "João Silva",
                "joao.silva@example.com",
                LocalDateTime.now()
        );

        when(customerService.getCustomerById(customerId)).thenReturn(response);

        mockMvc.perform(get("/customer/{customerId}", customerId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerId.toString()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.email").value(response.email()))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void deveRetornarClientePorIdDiferente() throws Exception {
        UUID customerId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        CustomerResponseDTO response = new CustomerResponseDTO(
                customerId,
                "Maria Santos",
                "maria.santos@example.com",
                LocalDateTime.of(2025, 1, 15, 10, 30)
        );

        when(customerService.getCustomerById(customerId)).thenReturn(response);

        mockMvc.perform(get("/customer/{customerId}", customerId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId.toString()))
                .andExpect(jsonPath("$.name").value("Maria Santos"))
                .andExpect(jsonPath("$.email").value("maria.santos@example.com"));
    }

    @Test
    void deveRetornarClientePorEmailComSucesso() throws Exception {
        // Arrange
        String email = "pedro.oliveira@example.com";
        CustomerResponseDTO response = new CustomerResponseDTO(
                UUID.randomUUID(),
                "Pedro Oliveira",
                email,
                LocalDateTime.now()
        );

        when(customerService.getCustomerByEmail(email)).thenReturn(response);

        mockMvc.perform(get("/customer/findByEmail")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deveRetornarClientePorEmailDiferente() throws Exception {
        String email = "ana.costa@hopin.com";
        CustomerResponseDTO response = new CustomerResponseDTO(
                UUID.fromString("660e8400-e29b-41d4-a716-446655440000"),
                "Ana Costa",
                email,
                LocalDateTime.now()
        );

        when(customerService.getCustomerByEmail(email)).thenReturn(response);

        mockMvc.perform(get("/customer/findByEmail")
                        .param("email", email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value("Ana Costa"));
    }

    @Test
    void deveEditarClienteComSucesso() throws Exception {
        CustomerRequestDTO request = new CustomerRequestDTO(
                "João Silva Atualizado",
                "joao.silva.novo@example.com",
                "novaSenha123",
                "1234567890123456",
                "123",
                "12/2027"
        );

        CustomerResponseDTO response = new CustomerResponseDTO(
                UUID.randomUUID(),
                request.name(),
                request.email(),
                LocalDateTime.now()
        );

        when(customerService.editCustomer(any(CustomerRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.email").value(request.email()))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deveEditarClienteSemDadosDeCartao() throws Exception {
        CustomerRequestDTO request = new CustomerRequestDTO(
                "Maria Santos",
                "maria.santos@example.com",
                "senha456",
                null,
                null,
                null
        );

        CustomerResponseDTO response = new CustomerResponseDTO(
                UUID.randomUUID(),
                request.name(),
                request.email(),
                LocalDateTime.now()
        );

        when(customerService.editCustomer(any(CustomerRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.email").value(request.email()));
    }

    @Test
    void deveEditarClienteComDadosCompletos() throws Exception {
        CustomerRequestDTO request = new CustomerRequestDTO(
                "Carlos Mendes",
                "carlos.mendes@hopin.com",
                "senhaSegura999",
                "9876543210987654",
                "456",
                "06/2028"
        );

        CustomerResponseDTO response = new CustomerResponseDTO(
                UUID.fromString("770e8400-e29b-41d4-a716-446655440000"),
                request.name(),
                request.email(),
                LocalDateTime.of(2026, 1, 28, 15, 30)
        );

        when(customerService.editCustomer(any(CustomerRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("770e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.name").value("Carlos Mendes"))
                .andExpect(jsonPath("$.email").value("carlos.mendes@hopin.com"));
    }

    @Test
    void deveDeletarClienteComSucesso() throws Exception {
        // Arrange
        UUID customerId = UUID.fromString("880e8400-e29b-41d4-a716-446655440000");

        doNothing().when(customerService).deleteCustomer(customerId);

        mockMvc.perform(delete("/customer/{customerId}", customerId.toString()))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(customerId);
    }

    @Test
    void deveDeletarClienteDiferente() throws Exception {
        UUID customerId = UUID.fromString("990e8400-e29b-41d4-a716-446655440000");

        doNothing().when(customerService).deleteCustomer(customerId);

        mockMvc.perform(delete("/customer/{customerId}", customerId.toString()))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(customerId);
    }

    @Test
    void deveRetornarClienteComIdEspecifico() throws Exception {
        // Arrange
        UUID customerId = UUID.fromString("111e8400-e29b-41d4-a716-446655440000");
        CustomerResponseDTO response = new CustomerResponseDTO(
                customerId,
                "Fernanda Lima",
                "fernanda.lima@test.com",
                LocalDateTime.of(2026, 1, 20, 8, 0)
        );

        when(customerService.getCustomerById(customerId)).thenReturn(response);

        mockMvc.perform(get("/customer/{customerId}", customerId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("111e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.name").value("Fernanda Lima"));
    }
}