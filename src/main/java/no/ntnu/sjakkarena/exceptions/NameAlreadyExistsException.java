package no.ntnu.sjakkarena.exceptions;

public class NameAlreadyExistsException extends RuntimeException {

    public NameAlreadyExistsException(String message) {
        super(message);
    }
}
