package br.edu.ifpb.hopin_daw2.core.domain.payments.exceptions;

public class NoCreditCardInfo extends RuntimeException {
    public NoCreditCardInfo(String message) {
        super(message);
    }
}
