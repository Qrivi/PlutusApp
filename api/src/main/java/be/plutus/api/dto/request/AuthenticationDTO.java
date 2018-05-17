package be.plutus.api.dto.request;

import org.hibernate.validator.constraints.NotBlank;

public class AuthenticationDTO{

    @NotBlank( message = "{NotBlank.AuthenticationDTO.cardNumber}" )
    private String cardNumber;

    @NotBlank( message = "{NotBlank.AuthenticationDTO.password}" )
    private String password;

    @NotBlank( message = "{NotBlank.AuthenticationDTO.client}" )
    private String client;

    public AuthenticationDTO(){
    }

    public String getCardNumber(){
        return cardNumber;
    }

    public String getPassword(){
        return password;
    }

    public String getClient(){
        return client;
    }
}
