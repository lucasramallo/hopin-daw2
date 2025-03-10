package br.edu.ifpb.hopin_daw2.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}