package be.plutus.core.exception;

public class InvalidLabelIdentifierException extends InvalidIdentifierException{

    public InvalidLabelIdentifierException(){
        super();
    }

    public InvalidLabelIdentifierException( String message ){
        super( message );
    }

    public InvalidLabelIdentifierException( Throwable throwable ){
        super( throwable );
    }

    public InvalidLabelIdentifierException( String message, Throwable exception ){
        super( message, exception );
    }
}