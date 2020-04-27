package no.ntnu.sjakkarena.exceptions;

/**
 * Thrown when data is not found in a database
 */
public class NotInDatabaseException extends RuntimeException {

    /**
     * Constructs a NotInDatabaseException with a message
     *
     * @param message A message describing the exception
     */
    public NotInDatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs a NotInDatabaseException with a cause
     *
     * @param cause The cause of the exception
     */
    public NotInDatabaseException(Throwable cause) {
        super(cause);
    }
}
