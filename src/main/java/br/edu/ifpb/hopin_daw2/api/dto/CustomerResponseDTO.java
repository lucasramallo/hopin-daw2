package br.edu.ifpb.hopin_daw2.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponseDTO(
        UUID id,
        String name,
        String email,
        String password,
        LocalDateTime createdAt
) {
}