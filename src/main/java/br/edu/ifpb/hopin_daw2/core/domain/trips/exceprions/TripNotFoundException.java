package br.edu.ifpb.hopin_daw2.core.domain.trips.exceprions;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException() {
        super("Trip not found!");
    }
}
