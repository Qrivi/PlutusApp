package be.plutus.core.service;

import be.plutus.common.DateService;
import be.plutus.core.exception.DuplicateCardException;
import be.plutus.core.exception.EmailTakenException;
import be.plutus.core.exception.InvalidCardIdentifierException;
import be.plutus.core.model.Card;
import be.plutus.core.model.CardLanguage;
import be.plutus.core.model.CardStatus;
import be.plutus.core.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CardJPAService implements CardService{

    private final CardRepository cardRepository;

    @Autowired
    public CardJPAService( CardRepository cardRepository ){
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> getAllCards(){
        return cardRepository.findAll();
    }

    @Override
    public Card getCardById( Integer id ){
        if( id == null )
            throw new InvalidCardIdentifierException();
        return cardRepository.findOne( id );
    }

    @Override
    public Card getCardByNumber( String number ){
        return cardRepository.findByNumberIgnoreCase( number );
    }

    @Override
    public Card getCardByEmail( String email ){
        return cardRepository.findByEmailIgnoreCase( email );
    }

    @Override
    public Card createCard( String customerId, String apiKey, String number, String name, String password, CardLanguage language, Double credit, Double weekSpent ){
        Card card = new Card();

        if( this.getCardByNumber( number ) != null )
            throw new DuplicateCardException( number );

        card.setCustomerId( customerId );
        card.setApiKey( apiKey );
        card.setNumber( number );
        card.setStatus( CardStatus.ACTIVE );
        card.setName( name );
        card.setCreationDate( DateService.now() );
        card.generateUuid();
        card.setPassword( password );
        card.setLanguage( language );
        card.setCredit( credit );
        card.setWeekSpent( weekSpent );
        card.setAlertLowCredit( -1 );

        return cardRepository.save( card );
    }

    @Override
    public void updateCardCredentials( int id, String customerId, String apiKey, String password ){
        Card card = this.getCardById( id );

        if( customerId != null )
            card.setCustomerId( customerId );
        if( apiKey != null )
            card.setApiKey( apiKey );
        if( password != null )
            card.setPassword( password );

        cardRepository.save( card );
    }

    @Override
    public void updateCardDetails( int id, String alias, String email, CardStatus status, CardLanguage language ){
        Card card = this.getCardById( id );

        Card unique = this.getCardByEmail( email );
        if( unique != null && unique != card )
            throw new EmailTakenException( email );

        if( alias != null )
            card.setAlias( alias );
        if( email != null )
            card.setEmail( email );
        if( status != null )
            card.setStatus( status );
        if( language != null )
            card.setLanguage( language );

        cardRepository.save( card );
    }

    @Override
    public void updateCardBalance( int id, Double credit, Double weekSpent ){
        Card card = this.getCardById( id );

        if( credit != null )
            card.setCredit( credit );
        if( weekSpent != null )
            card.setWeekSpent( weekSpent );

        cardRepository.save( card );
    }

    @Override
    public void updateCardAlerts( int id, Integer alertLowCredit ){
        Card card = this.getCardById( id );

        if( alertLowCredit != null )
            card.setAlertLowCredit( alertLowCredit );

        cardRepository.save( card );
    }

    @Override
    public void removeCard( int id ){
        Card card = this.getCardById( id );

        cardRepository.delete( card );
    }
}
