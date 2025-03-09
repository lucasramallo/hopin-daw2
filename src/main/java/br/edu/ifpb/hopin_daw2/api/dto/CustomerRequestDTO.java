package br.edu.ifpb.hopin_daw2.api.dto;

public record CustomerRequestDTO(
        String name,
        String email,
        String password,
        String creditCardNumber,
        String creditCardCVV,
        String creditCardExpiry
) {
}