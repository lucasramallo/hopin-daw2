package br.edu.ifpb.hopin_daw2.mappers;

import br.edu.ifpb.hopin_daw2.api.dto.TripResponseDTO;
import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {
    public static TripResponseDTO toDTO(Trip trip) {
        return new TripResponseDTO(
                trip.getId(),
                trip.getCustomer(),
                trip.getDriver(),
                trip.getPayment(),
                trip.getStatus(),
                trip.getSource(),
                trip.getDestination(),
                trip.getCreatedAt()
        );
    }

    public Trip toEntity(TripResponseDTO dto) {
        Trip trip = new Trip();

        trip.setId(dto.id());
        trip.setCustomer(dto.customer());
        trip.setDriver(dto.driver());
        trip.setPayment(dto.payment());
        trip.setStatus(dto.status());
        trip.setSource(trip.getSource());
        trip.setDestination(trip.getDestination());
        trip.setCreatedAt(trip.getCreatedAt());

        return trip;
    }
}
