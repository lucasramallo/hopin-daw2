package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.api.security.util.JwtProvider;
import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.role.Role;
import br.edu.ifpb.hopin_daw2.core.service.CustomerService;
import br.edu.ifpb.hopin_daw2.core.service.DriverService;
import br.edu.ifpb.hopin_daw2.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AuthController.class})
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerUTTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DriverService driverService;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtProvider jwtUtils;

    @MockitoBean
    private UserService userService;

    @Test
    void deveRealizarLoginComSucesso() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO(
                "user@example.com",
                "password123"
        );

        Customer mockUser = new Customer();
        mockUser.setEmail(loginRequest.email());
        mockUser.setName("Test User");
        mockUser.setPassword("encodedPassword");
        mockUser.setRole(Role.CUSTOMER);

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(
                mockUser,
                null,
                mockUser.getAuthorities()
        );

        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(jwtUtils.generateJwtToken(anyString())).thenReturn(expectedToken);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Autenticação realizada com sucesso!"))
                .andExpect(jsonPath("$.token").value(expectedToken));
    }

    @Test
    void deveRetornarUnauthorizedQuandoCredenciaisInvalidas() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO(
                "user@example.com",
                "wrongpassword"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"))
                .andExpect(jsonPath("$.token").isEmpty());
    }

    @Test
    void deveRetornarUnauthorizedQuandoErroDeAutenticacao() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO(
                "user@example.com",
                "password123"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Erro de autenticação") {});

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Erro de autenticação"))
                .andExpect(jsonPath("$.token").isEmpty());
    }

    @Test
    void deveCriarMotoristaComSucesso() throws Exception {
        CreateDriverRequestDTO driverRequest = new CreateDriverRequestDTO(
                "João Silva",
                "joao.silva@example.com",
                "senha123",
                LocalDate.of(1990, 5, 15),
                "Honda Civic",
                "Preto",
                "ABC-1234",
                "Banco do Brasil",
                "0001",
                "12345-6"
        );

        Cab cab = new Cab();
        cab.setModel(driverRequest.model());
        cab.setColor(driverRequest.color());
        cab.setPlateNum(driverRequest.plateNum());

        DriverResponseDTO expectedResponse = new DriverResponseDTO(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                driverRequest.name(),
                driverRequest.email(),
                driverRequest.dateOfBirth(),
                cab,
                LocalDateTime.now()
        );

        when(driverService.createDriver(any(CreateDriverRequestDTO.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/driver/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.id().toString()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()))
                .andExpect(jsonPath("$.dateOfBirth").value("15/05/1990"))
                .andExpect(jsonPath("$.cab").exists())
                .andExpect(jsonPath("$.cab.model").value(driverRequest.model()))
                .andExpect(jsonPath("$.cab.color").value(driverRequest.color()))
                .andExpect(jsonPath("$.cab.plateNum").value(driverRequest.plateNum()));
    }

    @Test
    void deveCriarMotoristaComDadosBancarios() throws Exception {
        CreateDriverRequestDTO driverRequest = new CreateDriverRequestDTO(
                "Maria Santos",
                "maria.santos@example.com",
                "senha456",
                LocalDate.of(1985, 8, 20),
                "Toyota Corolla",
                "Prata",
                "XYZ-5678",
                "Caixa Econômica",
                "0123",
                "98765-4"
        );

        Cab cab = new Cab();
        cab.setModel(driverRequest.model());
        cab.setColor(driverRequest.color());
        cab.setPlateNum(driverRequest.plateNum());

        DriverResponseDTO expectedResponse = new DriverResponseDTO(
                UUID.randomUUID(),
                driverRequest.name(),
                driverRequest.email(),
                driverRequest.dateOfBirth(),
                cab,
                LocalDateTime.now()
        );

        when(driverService.createDriver(any(CreateDriverRequestDTO.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/driver/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(driverRequest.name()))
                .andExpect(jsonPath("$.email").value(driverRequest.email()))
                .andExpect(jsonPath("$.cab.model").value(driverRequest.model()));
    }

    @Test
    void deveCriarClienteComSucesso() throws Exception {
        CustomerRequestDTO customerRequest = new CustomerRequestDTO(
                "Pedro Oliveira",
                "pedro.oliveira@example.com",
                "senha789",
                "1234567890123456",
                "123",
                "12/2025"
        );

        CustomerResponseDTO expectedResponse = new CustomerResponseDTO(
                UUID.fromString("660e8400-e29b-41d4-a716-446655440000"),
                customerRequest.name(),
                customerRequest.email(),
                LocalDateTime.now()
        );

        when(customerService.createCustomer(any(CustomerRequestDTO.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedResponse.id().toString()))
                .andExpect(jsonPath("$.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.email").value(expectedResponse.email()))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void deveCriarClienteSemDadosDeCartao() throws Exception {
        CustomerRequestDTO customerRequest = new CustomerRequestDTO(
                "Ana Costa",
                "ana.costa@example.com",
                "senha321",
                null,
                null,
                null
        );

        CustomerResponseDTO expectedResponse = new CustomerResponseDTO(
                UUID.randomUUID(),
                customerRequest.name(),
                customerRequest.email(),
                LocalDateTime.now()
        );

        when(customerService.createCustomer(any(CustomerRequestDTO.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(customerRequest.name()))
                .andExpect(jsonPath("$.email").value(customerRequest.email()));
    }

    @Test
    void deveCriarClienteComDadosCompletos() throws Exception {
        CustomerRequestDTO customerRequest = new CustomerRequestDTO(
                "Carlos Mendes",
                "carlos.mendes@example.com",
                "senhaSegura123",
                "9876543210987654",
                "456",
                "06/2027"
        );

        CustomerResponseDTO expectedResponse = new CustomerResponseDTO(
                UUID.randomUUID(),
                customerRequest.name(),
                customerRequest.email(),
                LocalDateTime.now()
        );

        when(customerService.createCustomer(any(CustomerRequestDTO.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(customerRequest.name()))
                .andExpect(jsonPath("$.email").value(customerRequest.email()))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void deveValidarEmailNoLogin() throws Exception {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO(
                "admin@hopin.com",
                "admin123"
        );

        Driver mockUser = new Driver();
        mockUser.setEmail(loginRequest.email());
        mockUser.setName("Admin Driver");
        mockUser.setPassword("encodedPassword");
        mockUser.setRole(Role.DRIVER);

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(
                mockUser,
                null,
                mockUser.getAuthorities()
        );

        String expectedToken = "token.jwt.test";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(jwtUtils.generateJwtToken(loginRequest.email())).thenReturn(expectedToken);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(expectedToken));
    }

    @Test
    void deveCriarMotoristaComDataNascimentoDiferente() throws Exception {
        CreateDriverRequestDTO driverRequest = new CreateDriverRequestDTO(
                "Roberto Alves",
                "roberto.alves@example.com",
                "senha999",
                LocalDate.of(1995, 12, 1),
                "Volkswagen Gol",
                "Branco",
                "DEF-9876",
                "Bradesco",
                "0456",
                "11111-1"
        );

        Cab cab = new Cab();
        cab.setModel(driverRequest.model());
        cab.setColor(driverRequest.color());
        cab.setPlateNum(driverRequest.plateNum());

        DriverResponseDTO expectedResponse = new DriverResponseDTO(
                UUID.randomUUID(),
                driverRequest.name(),
                driverRequest.email(),
                driverRequest.dateOfBirth(),
                cab,
                LocalDateTime.now()
        );

        when(driverService.createDriver(any(CreateDriverRequestDTO.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/driver/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dateOfBirth").value("01/12/1995"));
    }

    @Test
    void deveCriarClienteComEmailDiferente() throws Exception {
        CustomerRequestDTO customerRequest = new CustomerRequestDTO(
                "Fernanda Lima",
                "fernanda.lima@hopin.com",
                "senha555",
                "1111222233334444",
                "789",
                "03/2026"
        );

        CustomerResponseDTO expectedResponse = new CustomerResponseDTO(
                UUID.randomUUID(),
                customerRequest.name(),
                customerRequest.email(),
                LocalDateTime.now()
        );

        when(customerService.createCustomer(any(CustomerRequestDTO.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/auth/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(customerRequest.email()));
    }
}