package br.edu.ifpb.hopin_daw2.api.controllers;

import br.edu.ifpb.hopin_daw2.api.dto.EditRatingRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.RatingRequestDTO;
import br.edu.ifpb.hopin_daw2.api.dto.RatingResponseDTO;
import br.edu.ifpb.hopin_daw2.api.security.util.JwtProvider;
import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.rating.Rating;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import br.edu.ifpb.hopin_daw2.core.service.RatingService;
import br.edu.ifpb.hopin_daw2.core.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RatingControllerUTTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RatingService ratingService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @Test
    void deveCriarRatingComSucesso() throws Exception {
        UUID tripId = UUID.randomUUID();


        RatingRequestDTO request = new RatingRequestDTO(tripId, 5, "Excelente motorista!");


        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());

        Driver driver = new Driver();
        driver.setId(UUID.randomUUID());

        Trip trip = new Trip();
        trip.setId(tripId);


        RatingResponseDTO response = new RatingResponseDTO(
                UUID.randomUUID(),
                customer,
                driver,
                trip,
                5,
                "Excelente motorista!"
        );

        when(ratingService.save(any(RatingRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5)) // Nome do campo no record é 'rating'
                .andExpect(jsonPath("$.feedback").value("Excelente motorista!")); // Nome do campo no record é 'feedback'
    }

    @Test
    void deveBuscarRatingPorTripIdComSucesso() throws Exception {
        UUID tripId = UUID.randomUUID();

        Rating ratingEntity = new Rating();
        ratingEntity.setId(UUID.randomUUID());
        ratingEntity.setRating(4);
        ratingEntity.setFeedback("Muito bom");


        ratingEntity.setCustomer(new Customer());
        ratingEntity.setDriver(new Driver());
        ratingEntity.setTrip(new Trip());

        when(ratingService.getRatingByTripId(tripId)).thenReturn(Optional.of(ratingEntity));

        mockMvc.perform(get("/rating/{tripId}", tripId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.feedback").value("Muito bom"));
    }

    @Test
    void deveLancarExcecaoQuandoRatingNaoEncontrado() throws Exception {
        UUID tripId = UUID.randomUUID();

        when(ratingService.getRatingByTripId(tripId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/rating/{tripId}", tripId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    assertTrue(result.getResolvedException() instanceof RuntimeException);
                    assertEquals("No rating found", result.getResolvedException().getMessage());
                });
    }

    @Test
    void deveAtualizarRatingComSucesso() throws Exception {
        UUID ratingId = UUID.randomUUID();


        EditRatingRequestDTO request = new EditRatingRequestDTO(3, "Mudei de ideia.");


        Customer customer = new Customer();
        Driver driver = new Driver();
        Trip trip = new Trip();

        RatingResponseDTO response = new RatingResponseDTO(
                ratingId,
                customer,
                driver,
                trip,
                3,
                "Mudei de ideia."
        );

        when(ratingService.update(eq(ratingId), any(EditRatingRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/rating/{ratingId}", ratingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(3))
                .andExpect(jsonPath("$.feedback").value("Mudei de ideia."));
    }
}