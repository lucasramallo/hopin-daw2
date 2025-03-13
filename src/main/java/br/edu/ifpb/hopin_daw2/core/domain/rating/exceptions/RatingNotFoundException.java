package br.edu.ifpb.hopin_daw2.core.domain.rating.exceptions;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(String message) {
        super(message);
    }
}
