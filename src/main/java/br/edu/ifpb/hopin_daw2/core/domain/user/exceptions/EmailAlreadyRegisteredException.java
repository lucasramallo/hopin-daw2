package br.edu.ifpb.hopin_daw2.core.domain.user.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException() {
        super("Email already registered!");
    }
}
