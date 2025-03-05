package br.edu.ifpb.hopin_daw2.api.dto;

import java.util.UUID;

public record CabResponseDTO(
        UUID id,
        String model,
        String color,
        String plateNum
) {
}
