package be.plutus.core.exception;

public class InvalidIdentifierException extends RuntimeException{

    public InvalidIdentifierException(){
        super();
    }

    public InvalidIdentifierException( String message ){
        super( message );
    }

    public InvalidIdentifierException( Throwable throwable ){
        super( throwable );
    }

    public InvalidIdentifierException( String message, Throwable exception ){
        super( message, exception );
    }
}