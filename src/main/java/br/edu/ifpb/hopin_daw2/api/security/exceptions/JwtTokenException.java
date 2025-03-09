package br.edu.ifpb.hopin_daw2.api.security.exceptions;

public class JwtTokenException extends RuntimeException {
    public JwtTokenException(String message) {
        super(message);
    }
}
