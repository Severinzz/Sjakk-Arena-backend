package no.ntnu.sjakkarena.exceptions;

// code from: https://github.com/spring-guides/gs-uploading-files

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
