package br.edu.ifpb.hopin_daw2.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateDriverRequestDTO(
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String password,
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
