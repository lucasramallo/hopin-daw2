package br.edu.ifpb.hopin_daw2.core.domain.rating.util;

import br.edu.ifpb.hopin_daw2.api.dto.RatingResponseDTO;
import br.edu.ifpb.hopin_daw2.core.domain.rating.Rating;

public class RatingMapper {
    public static RatingResponseDTO toDTO(Rating rating) {
        return new RatingResponseDTO(
                rating.getId(),
                rating.getCustomer(),
                rating.getDriver(),
                rating.getTrip(),
                rating.getRating(),
                rating.getFeedback()
        );
    }
}
