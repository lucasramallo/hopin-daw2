package br.edu.ifpb.hopin_daw2.core.domain.cab.exceptions;

public class CabNotFoundException extends RuntimeException {
    public CabNotFoundException() {
        super("Cab not found!");
    }
}
