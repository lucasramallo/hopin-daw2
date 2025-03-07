package br.edu.ifpb.hopin_daw2.api.dto;

import br.edu.ifpb.hopin_daw2.core.domain.customer.Customer;
import br.edu.ifpb.hopin_daw2.core.domain.driver.Driver;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;

import java.util.UUID;

public record RatingResponseDTO(
        UUID id,
        Customer customer,
        Driver Driver,
        Trip trip,
        int rating,
        String feedback
) {
}
