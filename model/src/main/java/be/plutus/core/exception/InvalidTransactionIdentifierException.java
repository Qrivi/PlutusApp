package be.plutus.core.exception;

public class InvalidTransactionIdentifierException extends InvalidIdentifierException{

    public InvalidTransactionIdentifierException(){
        super();
    }

    public InvalidTransactionIdentifierException( String message ){
        super( message );
    }

    public InvalidTransactionIdentifierException( Throwable throwable ){
        super( throwable );
    }

    public InvalidTransactionIdentifierException( String message, Throwable exception ){
        super( message, exception );
    }
}