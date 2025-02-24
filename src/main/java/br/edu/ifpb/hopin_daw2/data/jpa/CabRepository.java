package br.edu.ifpb.hopin_daw2.data.jpa;

import br.edu.ifpb.hopin_daw2.core.domain.cab.Cab;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CabRepository extends JpaRepository<Cab, UUID> {
}
