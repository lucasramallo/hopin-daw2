package br.edu.ifpb.hopin_daw2.api.globalExceptions;

public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException(String message) {
        super(message);
    }
}
