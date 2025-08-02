package br.edu.ifpb.hopin_daw2.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate dateOfBirth,
        @NotBlank
        String model,
        @NotBlank
        String color,
        @NotBlank
        String plateNum,
        @NotBlank
        String bank,
        @NotBlank
        String bankBranch,
        @NotBlank
        String bankAccount
) {
}
