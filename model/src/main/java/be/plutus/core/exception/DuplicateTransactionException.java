package be.plutus.core.exception;

public class DuplicateTransactionException extends DuplicateException{

    public DuplicateTransactionException( Object key ){
        super( key );
        setMessage( "Transaction already exists" );
    }
}