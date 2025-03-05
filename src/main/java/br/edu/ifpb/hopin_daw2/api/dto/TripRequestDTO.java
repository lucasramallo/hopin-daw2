package br.edu.ifpb.hopin_daw2.api.dto;

import br.edu.ifpb.hopin_daw2.core.domain.payments.Method;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;

import java.math.BigDecimal;
import java.util.UUID;

public record TripRequestDTO(
        UUID customerId,
        UUID driverId,
        Method paymentMethod,
        BigDecimal paymentAmount,
        Status status,
        String origin,
        String destination
) {
}