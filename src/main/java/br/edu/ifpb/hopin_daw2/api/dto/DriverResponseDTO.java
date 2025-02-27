package br.edu.ifpb.hopin_daw2.api.dto;

import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record DriverResponseDTO(
        UUID id,
        String name,
        String email,
        LocalDate dateOfBirth,
        Cab cab,
        LocalDateTime createdAt
) {
}