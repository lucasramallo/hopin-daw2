package br.edu.ifpb.hopin_daw2.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequestDTO(
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String password,
        String creditCardNumber,
        String creditCardCVV,
        String creditCardExpiry
) {
}