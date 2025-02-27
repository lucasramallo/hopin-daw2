package br.edu.ifpb.hopin_daw2.api.globalExceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
