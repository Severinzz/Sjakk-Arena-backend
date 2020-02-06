package no.ntnu.sjakkarena.Exceptions;

public class NotAbleToInsertIntoDBException extends RuntimeException{
    public NotAbleToInsertIntoDBException(String message) {
        super(message);
    }
}
