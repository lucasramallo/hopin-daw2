package br.edu.ifpb.hopin_daw2.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RatingRequestDTO(
        @NotNull
        UUID tripId,
        @Min(0)
        @Max(5)
        int rating,
        String feedback
) {
}
