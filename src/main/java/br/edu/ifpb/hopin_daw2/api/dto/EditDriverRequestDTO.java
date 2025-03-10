package br.edu.ifpb.hopin_daw2.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EditDriverRequestDTO(
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotNull
        LocalDate dateOfBirth,
        @NotBlank
        String model,
        @NotBlank
        String color,
        @NotBlank
        String plateNum
) {
}
