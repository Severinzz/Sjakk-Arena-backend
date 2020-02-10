package no.ntnu.sjakkarena.exceptions;

public class NotAbleToInsertIntoDBException extends RuntimeException{
    public NotAbleToInsertIntoDBException(String message) {
        super(message);
    }
}
