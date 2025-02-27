package br.edu.ifpb.hopin_daw2.api.dto;

import java.time.LocalDate;

public record CreateDriverRequestDTO(
        String name,
        String email,
        String password,
        LocalDate dateOfBirth,
        String model,
        String color,
        String plateNum
) {
}
