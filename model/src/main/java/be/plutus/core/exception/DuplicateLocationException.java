package be.plutus.core.exception;

public class DuplicateLocationException extends DuplicateException{

    public DuplicateLocationException( Object key ){
        super( key );
        setMessage( "Location already exists" );
    }
}