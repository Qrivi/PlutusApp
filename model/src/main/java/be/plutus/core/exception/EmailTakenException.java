package be.plutus.core.exception;

public class EmailTakenException extends RuntimeException{

    private String email;
    private String message;

    public EmailTakenException( String email ){
        super();
        this.email = email;
        this.message = "The provided e-mail address is already linked to an existing account";
    }

    public Object getEmail(){
        return email;
    }

    @Override
    public String getMessage(){
        return message;
    }

    protected void setMessage( String message ){
        this.message = message;
    }
}