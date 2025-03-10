package br.edu.ifpb.hopin_daw2.core.domain.rating.exceptions;

public class RatingNotFound extends RuntimeException {
    public RatingNotFound() {
        super("Rating not found!");
    }
}
