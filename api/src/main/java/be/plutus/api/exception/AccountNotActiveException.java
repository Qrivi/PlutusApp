package be.plutus.api.exception;

public class AccountNotActiveException extends AuthenticationException{
    public AccountNotActiveException(){
        super( 403 );
    }
}
