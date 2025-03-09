package br.edu.ifpb.hopin_daw2.api.dto;

public record LoginRequestDTO(
        String email,
        String password
) {
}