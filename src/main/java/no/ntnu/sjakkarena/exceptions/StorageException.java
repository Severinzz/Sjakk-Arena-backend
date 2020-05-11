package no.ntnu.sjakkarena.exceptions;

public class StorageException extends RuntimeException {
    /**
     * Constructs a StorageException with a message
     *
     * @param message A message describing the exception
     */
    public StorageException(String message) { super(message); }

    /**
     * Constructs a StorageException with a cause
     *
     * @param cause The cause of the exception
     */
    public StorageException(String message, Throwable cause) { super(message, cause); }
}
