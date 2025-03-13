package br.edu.ifpb.hopin_daw2.core.domain.trips.exceptions;

public class InvalidTripStatusUpdateException extends RuntimeException {
    public InvalidTripStatusUpdateException(String previousStatus, String newStatus) {
        super("The trip with status " + previousStatus + " cannot be updated to the new status " + newStatus + "!");
    }
}
