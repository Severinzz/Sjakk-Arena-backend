package no.ntnu.sjakkarena.exceptions;

/**
 * Thrown when a problem with updating a database has occurred
 */
public class TroubleUpdatingDBException extends RuntimeException{

    /**
     * Constructs a TroubleUpdatingDBException with a message
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     *                (description from https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html)
     */
    public TroubleUpdatingDBException(String message) {
        super(message);
    }

    /**
     * Constructs a TroubleUpdatingDBException with a cause
     *
     * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method).
     *             (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     *              (description from https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html)
     */
    public TroubleUpdatingDBException(Throwable cause) {
        super(cause);
    }
}
