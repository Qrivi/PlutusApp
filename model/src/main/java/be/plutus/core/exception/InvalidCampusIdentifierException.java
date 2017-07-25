package be.plutus.core.exception;

public class InvalidCampusIdentifierException extends InvalidIdentifierException{

    public InvalidCampusIdentifierException(){
        super();
    }

    public InvalidCampusIdentifierException( String message ){
        super( message );
    }

    public InvalidCampusIdentifierException( Throwable throwable ){
        super( throwable );
    }

    public InvalidCampusIdentifierException( String message, Throwable exception ){
        super( message, exception );
    }
}