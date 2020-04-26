package no.ntnu.sjakkarena.exceptions;

/**
 * Thrown when data is not found in a database
 */
public class NotInDatabaseException extends RuntimeException {

    /**
     * Constructs a NotInDatabaseException with a message
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     *                (description from https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html)
     */
    public NotInDatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs a NotInDatabaseException with a cause
     *
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method).
     *             (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     *              (description from https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html)
     */
    public NotInDatabaseException(Throwable cause) {
        super(cause);
    }
}
