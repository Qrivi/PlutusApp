package be.plutus.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ApiKeyDTO{

    @JsonProperty( "resultCode" )
    private String result;

    @JsonProperty( "customerId" )
    private String customerId;

    @JsonProperty( "apiKey" )
    private String key;

    public ApiKeyDTO(){
    }

    public String getResult(){
        return result;
    }

    public String getCustomerId(){
        return customerId;
    }

    public String getKey(){
        return key;
    }
}
