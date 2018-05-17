package be.plutus.api.exception;

public class IllegalParameterException extends RuntimeException{

    private Object parameterValue;
    private String parameterName;
    private String message;

    public IllegalParameterException( Object parameterValue ){
        this( parameterValue, null );
    }

    public IllegalParameterException( Object parameterValue, String parameterName ){
        super();
        this.parameterValue = parameterValue;
        this.parameterName = parameterName;
        this.message = "A parameter with an illegal value was passed through";
    }

    public Object getParameterValue(){
        return parameterValue;
    }

    protected void setParameterValue( Object parameterValue ){
        this.parameterValue = parameterValue;
    }

    public String getParameterName(){
        return parameterName;
    }

    protected void setParameterName( String parameterName ){
        this.parameterName = parameterName;
    }

    @Override
    public String getMessage(){
        return message;
    }

    protected void setMessage( String message ){
        this.message = message;
    }
}