package no.ntnu.sjakkarena.exceptions;

public class NotInDatabaseException extends RuntimeException {

    public NotInDatabaseException(String message){
        super(message);
    }
}
