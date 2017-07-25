package be.plutus.core.exception;

public class EncryptionException extends RuntimeException{

    public EncryptionException(){
        super();
    }

    public EncryptionException( String message ){
        super( message );
    }

    public EncryptionException( Throwable throwable ){
        super( throwable );
    }

    public EncryptionException( String message, Throwable exception ){
        super( message, exception );
    }
}