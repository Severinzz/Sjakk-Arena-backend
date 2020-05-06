package no.ntnu.sjakkarena.exceptions;

/**
 * Thrown when a suggested (player) name already exists
 */
public class NameAlreadyExistsException extends RuntimeException {

    /**
     * Constructs an NameAlreadyExistsException
     *
     * @param message A message describing the exception
     */
    public NameAlreadyExistsException(String message) {
        super(message);
    }
}
