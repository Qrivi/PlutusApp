package be.plutus.core.exception;

public class InvalidTokenIdentifierException extends InvalidIdentifierException{

    public InvalidTokenIdentifierException(){
        super();
    }

    public InvalidTokenIdentifierException( String message ){
        super( message );
    }

    public InvalidTokenIdentifierException( Throwable throwable ){
        super( throwable );
    }

    public InvalidTokenIdentifierException( String message, Throwable exception ){
        super( message, exception );
    }
}