package br.edu.ifpb.hopin_daw2.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record EditRatingRequestDTO(
        @Min(0)
        @Max(5)
        int rating,
        String feedback
) {
}
