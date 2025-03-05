package br.edu.ifpb.hopin_daw2.api.dto;

import java.util.List;

public record DriverTripsResponseDTO(
        List<TripResponseDTO> trips
) {
}