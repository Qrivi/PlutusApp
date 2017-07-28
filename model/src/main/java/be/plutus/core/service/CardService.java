package be.plutus.core.service;

import be.plutus.core.model.Card;
import be.plutus.core.model.CardEmailStatus;
import be.plutus.core.model.CardLanguage;
import be.plutus.core.model.CardStatus;

import java.util.List;

public interface CardService{

    List<Card> getAllCards();

    Card getCardById( Integer id );

    Card getCardByEmail( String email );

    Card createCard( String name, String alias, CardLanguage language, double credit, double weekSpent );

    void updateCardDetails( int id, String alias, CardLanguage language, CardStatus status, CardEmailStatus emailStatus, String email );

    void updateCardBalance( int id, Double credit, Double weekSpent );

    void updateCardAlerts( int id, Integer alertLowCredit );

    void removeCard( int id );
}
