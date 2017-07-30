package be.plutus.core.exception;

public class DuplicateCredentialsException extends DuplicateException{

    public DuplicateCredentialsException( Object key ){
        super( key );
        setMessage( "Credentials already exist" );
    }
}