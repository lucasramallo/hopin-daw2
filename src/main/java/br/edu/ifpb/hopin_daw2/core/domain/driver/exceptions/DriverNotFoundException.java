package br.edu.ifpb.hopin_daw2.core.domain.driver.exceptions;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException() {
        super("Driver not found!");
    }
}
