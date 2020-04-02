package no.ntnu.sjakkarena.exceptions;

public class TroubleUpdatingDBException extends RuntimeException{
    public TroubleUpdatingDBException(String message) {
        super(message);
    }

    public TroubleUpdatingDBException(Throwable cause) {
        super(cause);
    }
}
