package br.edu.ifpb.hopin_daw2.data.jpa;

import br.edu.ifpb.hopin_daw2.core.domain.trips.Trip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {
    ArrayList<Trip> findAllByCustomerId(UUID customerId, Pageable pageable);

    ArrayList<Trip> findAllByDriverId(UUID driverId, Pageable pageable);
}