package no.ntnu.sjakkarena.exceptions;

/**
 * Thrown when a problem with updating a database has occurred
 */
public class TroubleUpdatingDBException extends RuntimeException{

    /**
     * Constructs a TroubleUpdatingDBException with a message
     *
     * @param message A message describing the exception
     */
    public TroubleUpdatingDBException(String message) {
        super(message);
    }

    /**
     * Constructs a TroubleUpdatingDBException with a cause
     *
     * @param cause The cause of the exception
     */
    public TroubleUpdatingDBException(Throwable cause) {
        super(cause);
    }
}
