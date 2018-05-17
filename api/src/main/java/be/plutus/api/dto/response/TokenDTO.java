package be.plutus.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.ZonedDateTime;

@JsonPropertyOrder( {
        "token",
        "expires",
        "expiresISO8601"
} )
public class TokenDTO{

    private String token;
    private ZonedDateTime expires;

    public TokenDTO(){
    }

    public String getToken(){
        return token;
    }

    public void setToken( String token ){
        this.token = token;
    }

    public ZonedDateTime getExpires(){
        return expires;
    }

    public void setExpires( ZonedDateTime expires ){
        this.expires = expires;
    }

    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ" )
    public ZonedDateTime getExpiresISO8601(){
        return expires;
    }
}