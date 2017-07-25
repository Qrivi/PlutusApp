package be.plutus.core.exception;

public class InvalidProductIdentifierException extends InvalidIdentifierException{

    public InvalidProductIdentifierException(){
        super();
    }

    public InvalidProductIdentifierException( String message ){
        super( message );
    }

    public InvalidProductIdentifierException( Throwable throwable ){
        super( throwable );
    }

    public InvalidProductIdentifierException( String message, Throwable exception ){
        super( message, exception );
    }
}