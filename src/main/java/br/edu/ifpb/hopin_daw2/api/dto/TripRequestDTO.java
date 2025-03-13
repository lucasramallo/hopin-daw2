package br.edu.ifpb.hopin_daw2.api.dto;

import br.edu.ifpb.hopin_daw2.core.domain.payments.Method;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TripRequestDTO(
        @NotNull
        UUID driverId,
        @NotNull
        Method paymentMethod,
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal paymentAmount,
        @NotBlank
        String origin,
        @NotBlank
        String destination
) {
}