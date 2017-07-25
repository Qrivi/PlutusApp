package be.plutus.core.exception;

public class InvalidCardIdentifierException extends InvalidIdentifierException{

    public InvalidCardIdentifierException(){
        super();
    }

    public InvalidCardIdentifierException( String message ){
        super( message );
    }

    public InvalidCardIdentifierException( Throwable throwable ){
        super( throwable );
    }

    public InvalidCardIdentifierException( String message, Throwable exception ){
        super( message, exception );
    }
}