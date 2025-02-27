package br.edu.ifpb.hopin_daw2.api.globalExceptions;

public class RequiredFieldsException extends RuntimeException {
    public RequiredFieldsException(String message) {
        super(message);
    }
}
