package no.ntnu.sjakkarena.exceptions;

public class NotAbleToUpdateDBException extends RuntimeException{
    public NotAbleToUpdateDBException(String message) {
        super(message);
    }
}
