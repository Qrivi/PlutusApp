package be.plutus.core.service;

import be.plutus.common.IdentifierService;
import be.plutus.core.exception.DuplicateCredentialsException;
import be.plutus.core.exception.InvalidCredentialsIdentifierException;
import be.plutus.core.model.Card;
import be.plutus.core.model.Credentials;
import be.plutus.core.model.CredentialsIdentity;
import be.plutus.core.model.CredentialsStatus;
import be.plutus.core.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CredentialsJPAService implements CredentialsService{

    private final CredentialsRepository repository;

    @Autowired
    public CredentialsJPAService( CredentialsRepository repository ){
        this.repository = repository;
    }

    @Override
    public List<Credentials> getAllCredentials(){
        return repository.findAll();
    }

    @Override
    public Credentials getCredentialsById( Integer id ){
        if( id == null )
            throw new InvalidCredentialsIdentifierException();
        return repository.findOne( id );
    }

    @Override
    public Credentials getCredentialsByCardNumber( String cardNumber ){
        return repository.findByCardNumberIgnoreCase( cardNumber );
    }

    @Override
    public Credentials generateCredentials( Credentials credentials, String cardNumber, String password ){

        if( credentials == null)
            credentials = new Credentials();

        if( this.getCredentialsByCardNumber( cardNumber ) != null )
            throw new DuplicateCredentialsException( cardNumber );

        CredentialsIdentity identity = IdentifierService.generateIdentity();

        credentials.setCardNumber( cardNumber );
        credentials.setPassword( password );
        credentials.setStatus( CredentialsStatus.NEW );
        credentials.setDeviceId( identity.toUUID() );
        credentials.setDeviceName( identity.toDeviceModel() );
        credentials.setUserAgent( identity.toUserAgent() );

        return credentials;
    }

    @Override
    public Credentials saveCredentials( Credentials credentials ){

        credentials.setStatus( CredentialsStatus.CORRECT );
        return repository.save( credentials );
    }

    @Override
    public void updateCredentials( int id, String password, Integer customerId, String key, String deviceId, String deviceName, String userAgent, Card card ){
        Credentials credentials = this.getCredentialsById( id );

        if( password != null )
            credentials.setPassword( password );
        if( customerId != null )
            credentials.setCustomerId( customerId );
        if( key != null )
            credentials.setKey( key );
        if( deviceId != null )
            credentials.setDeviceId( deviceId );
        if( deviceName != null )
            credentials.setDeviceName( deviceName );
        if( userAgent != null )
            credentials.setUserAgent( userAgent );

        repository.save( credentials );
    }

    @Override
    public void removeCredentials( int id ){
        Credentials credentials = this.getCredentialsById( id );

        repository.delete( credentials );
    }
}
