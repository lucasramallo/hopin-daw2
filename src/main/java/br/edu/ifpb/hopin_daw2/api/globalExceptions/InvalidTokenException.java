package br.edu.ifpb.hopin_daw2.api.globalExceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Invalid token!");
    }
}
