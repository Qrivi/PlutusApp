package be.plutus.core.service;

import be.plutus.common.DateService;
import be.plutus.core.exception.DuplicateCardException;
import be.plutus.core.exception.InvalidCardIdentifierException;
import be.plutus.core.model.*;
import be.plutus.core.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CardJPAService implements CardService{

    private final CardRepository repository;

    @Autowired
    public CardJPAService( CardRepository repository ){
        this.repository = repository;
    }

    @Override
    public List<Card> getAllCards(){
        return repository.findAll();
    }

    @Override
    public Card getCardById( Integer id ){
        if( id == null )
            throw new InvalidCardIdentifierException();
        return repository.findOne( id );
    }

    @Override
    public Card getCardByEmail( String email ){
        return repository.findByEmailIgnoreCase( email );
    }

    @Override
    public Card getCardByCredentials( Credentials credentials ){
        return repository.findByCredentials( credentials );
    }

    @Override
    public Card createCard( Credentials credentials, String name, String alias, CardLanguage language, double credit, double weekSpent ){
        Card card = new Card();

        card.setCredentials( credentials );
        card.setName( name );
        card.setAlias( alias );
        card.setLanguage( language );
        card.setStatus( CardStatus.NEW );
        card.setEmailStatus( CardEmailStatus.NOT_SET );
        card.setCreationDate( DateService.now() );

        // parameters are primitive to avoid null and potential future nullpointerexceptions
        card.setCredit( credit );
        card.setWeekSpent( weekSpent );
        card.setAlertLowCredit( -1 );

        return repository.save( card );
    }

    @Override
    public void updateCardDetails( int id, String alias, CardLanguage language, CardStatus status, CardEmailStatus emailStatus, String email ){
        Card card = this.getCardById( id );

        Card unique = this.getCardByEmail( email );
        if( unique != null && unique != card )
            throw new DuplicateCardException( email );

        if( alias != null )
            card.setAlias( alias );
        if( language != null )
            card.setLanguage( language );
        if( status != null )
            card.setStatus( status );
        if( emailStatus != null )
            card.setEmailStatus( emailStatus );
        if( email != null )
            card.setEmail( email );

        repository.save( card );
    }

    @Override
    public void updateCardBalance( int id, Double credit, Double weekSpent ){
        Card card = this.getCardById( id );

        if( credit != null )
            card.setCredit( credit );
        if( weekSpent != null )
            card.setWeekSpent( weekSpent );

        repository.save( card );
    }

    @Override
    public void updateCardAlerts( int id, Integer alertLowCredit ){
        Card card = this.getCardById( id );

        if( alertLowCredit != null )
            card.setAlertLowCredit( alertLowCredit );

        repository.save( card );
    }

    @Override
    public void removeCard( int id ){
        Card card = this.getCardById( id );

        repository.delete( card );
    }
}
