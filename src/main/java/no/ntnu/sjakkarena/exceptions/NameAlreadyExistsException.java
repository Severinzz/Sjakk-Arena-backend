package no.ntnu.sjakkarena.exceptions;

/**
 * Thrown when a suggested (player) name already exists
 */
public class NameAlreadyExistsException extends RuntimeException {

    /**
     * Constructs an NameAlreadyExistsException
     *
     * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
     *             (description from https://docs.oracle.com/javase/8/docs/api/java/lang/RuntimeException.html)
     */
    public NameAlreadyExistsException(String message) {
        super(message);
    }
}
