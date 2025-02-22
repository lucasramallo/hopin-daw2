package br.edu.ifpb.hopin_daw2.core.domain.customer.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super("Customer not found!");
    }
}
