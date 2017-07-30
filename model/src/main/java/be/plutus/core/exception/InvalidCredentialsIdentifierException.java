package be.plutus.core.exception;

public class InvalidCredentialsIdentifierException extends InvalidIdentifierException{

    public InvalidCredentialsIdentifierException(){
        super();
    }

    public InvalidCredentialsIdentifierException( String message ){
        super( message );
    }

    public InvalidCredentialsIdentifierException( Throwable throwable ){
        super( throwable );
    }

    public InvalidCredentialsIdentifierException( String message, Throwable exception ){
        super( message, exception );
    }
}