package br.edu.ifpb.hopin_daw2.api.dto;

import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.payments.Payment;
import br.edu.ifpb.hopin_daw2.core.domain.rating.Rating;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record TripResponseDTO(
        UUID id,
        Customer customer,
        Driver driver,
        Payment payment,
        Status status,
        String origin,
        String destination,
        LocalDateTime createdAt
) {
}