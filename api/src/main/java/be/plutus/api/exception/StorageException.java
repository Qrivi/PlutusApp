package be.plutus.api.exception;

public class StorageException extends RuntimeException{

    public StorageException(){
        super();
    }

    public StorageException( String message ){
        super( message );
    }

    public StorageException( Throwable throwable ){
        super( throwable );
    }

    public StorageException( String message, Throwable exception ){
        super( message, exception );
    }
}