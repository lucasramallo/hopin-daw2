package br.edu.ifpb.hopin_daw2.api.dto;

import java.time.LocalDate;

public record EditDriverRequestDTO(
        String name,
        LocalDate dateOfBirth
) {
}
