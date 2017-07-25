package be.plutus.core.exception;

public class DuplicateException extends RuntimeException{

    private Object key;
    private String message;

    public DuplicateException( Object key ){
        super();
        this.key = key;
        this.message = "Object already exists";
    }

    public Object getKey(){
        return key;
    }

    @Override
    public String getMessage(){
        return message;
    }

    protected void setMessage( String message ){
        this.message = message;
    }
}