package no.ntnu.sjakkarena.exceptions;

/**
 * Thrown when a user is not subscribing to a STOMP-destination.
 */
public class NotSubscribingException extends RuntimeException {

    /**
     * Constructs a NotSubscribingException with a message and a cause
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     *                (description from https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html)
     * @param cause   the cause (which is saved for later retrieval by the Throwable.getCause() method).
     *                (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     *                (description from https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html)
     */
    public NotSubscribingException(String message, Throwable cause) {
        super(message, cause);
    }
}
