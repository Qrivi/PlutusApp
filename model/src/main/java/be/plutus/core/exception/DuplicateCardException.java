package be.plutus.core.exception;

public class DuplicateCardException extends DuplicateException{

    public DuplicateCardException( Object key ){
        super( key );
        setMessage( "Card already exists" );
    }
}