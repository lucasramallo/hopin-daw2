package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.*;
import br.edu.ifpb.hopin_daw2.api.security.util.JwtProvider;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.role.Role;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;
import br.edu.ifpb.hopin_daw2.core.service.DriverService;
import br.edu.ifpb.hopin_daw2.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DriverController.class)
@AutoConfigureMockMvc(addFilters = false)
class DriverControllerUTTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DriverService driverService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Deve retornar motorista por ID com sucesso (Retorno DTO)")
    void deveRetornarMotoristaPorIdComSucesso() throws Exception {
        UUID driverId = UUID.randomUUID();
        
        DriverResponseDTO response = new DriverResponseDTO(
                driverId, "João Motorista", "joao@hopin.com",
                LocalDate.of(1990, 5, 10), null, LocalDateTime.now()
        );

        when(driverService.getDriverById(any(UUID.class))).thenReturn(response);

        mockMvc.perform(get("/driver/{driverId}", driverId)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(driverId.toString()))
                .andExpect(jsonPath("$.name").value("João Motorista"));
    }

    @Test
    @DisplayName("Deve retornar motorista por Email com sucesso (Retorno Entity)")
    void deveRetornarMotoristaPorEmailComSucesso() throws Exception {
        String email = "driver@hopin.com";
        Driver driver = createValidDriver("Motorista Email", email);
        
        when(driverService.getDriverByEmail(anyString())).thenReturn(driver);

        mockMvc.perform(get("/driver/getData/{driverEmail}", email)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @DisplayName("Deve retornar histórico de viagens paginado")
    void deveRetornarHistoricoDeViagens() throws Exception {
        TripResponseDTO trip = new TripResponseDTO(
                UUID.randomUUID(), null, null, null,
                Status.COMPLETED, "Centro", "Aeroporto", LocalDateTime.now()
        );

        Page<TripResponseDTO> page = new PageImpl<>(List.of(trip), PageRequest.of(0, 10), 1);
        
        when(driverService.getTripsHistory(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/driver/getTripsHistory")
                        .with(user("driverUser").roles("DRIVER"))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].origin").value("Centro"));
    }

    @Test
    @DisplayName("Deve retornar solicitações de viagem do motorista")
    void deveRetornarSolicitacoesDeViagemDoMotorista() throws Exception {
        UUID driverId = UUID.randomUUID();
        
        TripResponseDTO trip = new TripResponseDTO(
                UUID.randomUUID(), null, null, null,
                Status.REQUESTED, "Shopping", "Universidade", LocalDateTime.now()
        );

        Page<TripResponseDTO> page = new PageImpl<>(List.of(trip), PageRequest.of(0, 10), 1);
        
        when(driverService.getTripsRequests(any(UUID.class), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/driver/getTripsRequests/{driverId}", driverId)
                        .with(user("driverUser").roles("DRIVER"))
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value("REQUESTED"));
    }

    @Test
    @DisplayName("Deve retornar informações do veículo (Cab)")
    void deveRetornarCabDoMotorista() throws Exception {
        UUID driverId = UUID.randomUUID();
        CabResponseDTO cab = new CabResponseDTO(
                UUID.randomUUID(), "Toyota Corolla", "Prata", "ABC-1234"
        );

        when(driverService.getDriverCab(any(UUID.class))).thenReturn(cab);

        mockMvc.perform(get("/driver/cab/{driverId}", driverId)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Toyota Corolla"));
    }

    @Test
    @DisplayName("Deve retornar lista de todos os motoristas")
    void deveRetornarTodosOsMotoristas() throws Exception {
        Driver d1 = createValidDriver("Driver 1", "d1@email.com");
        Driver d2 = createValidDriver("Driver 2", "d2@email.com");

        when(driverService.getAll()).thenReturn(List.of(d1, d2));

        mockMvc.perform(get("/driver/drivers")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Deve editar motorista com sucesso")
    void deveEditarMotoristaComSucesso() throws Exception {
        EditDriverRequestDTO request = new EditDriverRequestDTO(
                "Motorista Atualizado", "novo@hopin.com",
                LocalDate.of(1988, 3, 22), "Honda Civic", "Preto", "XYZ-9999"
        );

        DriverResponseDTO response = new DriverResponseDTO(
                UUID.randomUUID(), request.name(), request.email(),
                request.dateOfBirth(), null, LocalDateTime.now()
        );

        when(driverService.editDriver(any(EditDriverRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/driver")
                        .with(user("driverUser").roles("DRIVER"))
                        .with(csrf()) 
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Motorista Atualizado"));
    }

    @Test
    @DisplayName("Deve deletar motorista com sucesso (No Content)")
    void deveDeletarMotoristaComSucesso() throws Exception {
        UUID driverId = UUID.randomUUID();
        
        when(driverService.deleteDriver(any(UUID.class))).thenReturn(true);

        mockMvc.perform(delete("/driver/{driverId}", driverId)
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
        ).andExpect(status().isNoContent());

        verify(driverService).deleteDriver(any(UUID.class));
    }

    private Driver createValidDriver(String name, String email) {
        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());
        driver.setName(name);
        driver.setEmail(email);
        driver.setPassword("defaultPassword123"); 
        driver.setDateOfBirth(LocalDate.of(1990, 1, 1));
        driver.setCreatedAt(LocalDateTime.now());
        driver.setRole(Role.DRIVER); 
        driver.setCab(null); // Importante: null para evitar loop infinito no JSON
        return driver;
    }
}