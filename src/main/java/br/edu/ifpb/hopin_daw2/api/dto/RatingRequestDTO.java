package br.edu.ifpb.hopin_daw2.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RatingRequestDTO(
        @NotBlank
        UUID customerId,
        @NotBlank
        UUID driverId,
        @NotBlank
        UUID tripId,
        @NotBlank
        int rating,
        String feedback
) {
}
