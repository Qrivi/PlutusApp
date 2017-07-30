package be.plutus.core.service;

import be.plutus.core.model.Card;
import be.plutus.core.model.Credentials;

import java.util.List;

public interface CredentialsService{
    List<Credentials> getAllCredentials();

    Credentials getCredentialsById( Integer id );

    Credentials getCredentialsByCardNumber( String cardNumber );

    Credentials generateCredentials( Credentials credentials, String cardNumber, String password );

    Credentials saveCredentials( Credentials credentials );

    void updateCredentials( int id, String password, Integer customerId, String key, String deviceId, String deviceName, String userAgent, Card card );

    void removeCredentials( int id );
}
