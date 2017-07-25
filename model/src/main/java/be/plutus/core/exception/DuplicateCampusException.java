package be.plutus.core.exception;

public class DuplicateCampusException extends DuplicateException{

    public DuplicateCampusException( Object key ){
        super( key );
        setMessage( "Campus already exists" );
    }
}