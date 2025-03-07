package br.edu.ifpb.hopin_daw2.data.jpa;

import br.edu.ifpb.hopin_daw2.core.domain.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
}
