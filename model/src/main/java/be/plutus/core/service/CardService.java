package be.plutus.core.service;

import be.plutus.core.model.Card;
import be.plutus.core.model.CardLanguage;
import be.plutus.core.model.CardStatus;

import java.util.List;

public interface CardService{

    List<Card> getAllCards();

    Card getCardById( Integer id );

    Card getCardByNumber( String number );

    Card getCardByEmail( String email );

    Card createCard( String customerId, String apiKey, String number, String name, String password, CardLanguage language, Double credit, Double weekSpent );

    void updateCardCredentials( int id, String customerId, String apiKey, String password );

    void updateCardDetails( int id, String alias, String email, CardStatus status, CardLanguage language );

    void updateCardBalance( int id, Double credit, Double weekSpent );

    void updateCardAlerts( int id, Integer alertLowCredit );

    void removeCard( int id );
}
