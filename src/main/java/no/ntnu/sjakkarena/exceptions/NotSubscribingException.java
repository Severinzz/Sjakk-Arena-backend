package no.ntnu.sjakkarena.exceptions;

/**
 * Thrown when a user is not subscribing to a STOMP-destination.
 */
public class NotSubscribingException extends RuntimeException {

    /**
     * Constructs a NotSubscribingException with a message and a cause
     *
     * @param message A message describing the exception
     * @param cause   The cause of the exception
     */
    public NotSubscribingException(String message, Throwable cause) {
        super(message, cause);
    }
}
