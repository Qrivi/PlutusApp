package be.plutus.core.exception;

public class DuplicateLabelException extends DuplicateException{

    public DuplicateLabelException( Object key ){
        super( key );
        setMessage( "Label already exists" );
    }
}