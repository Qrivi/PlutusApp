package be.plutus.core.exception;

public class InvalidLocationIdentifierException extends InvalidIdentifierException{

    public InvalidLocationIdentifierException(){
        super();
    }

    public InvalidLocationIdentifierException( String message ){
        super( message );
    }

    public InvalidLocationIdentifierException( Throwable throwable ){
        super( throwable );
    }

    public InvalidLocationIdentifierException( String message, Throwable exception ){
        super( message, exception );
    }
}