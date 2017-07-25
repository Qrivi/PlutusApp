package be.plutus.core.exception;

public class DuplicateProductException extends DuplicateException{

    public DuplicateProductException( Object key ){
        super( key );
        setMessage( "Product already exists" );
    }
}