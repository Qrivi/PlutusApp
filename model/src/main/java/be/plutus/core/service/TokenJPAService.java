package be.plutus.core.service;

import be.plutus.common.DateService;
import be.plutus.core.config.Config;
import be.plutus.core.exception.InvalidTokenIdentifierException;
import be.plutus.core.model.Card;
import be.plutus.core.model.Request;
import be.plutus.core.model.Token;
import be.plutus.core.model.TokenStatus;
import be.plutus.core.repository.RequestRepository;
import be.plutus.core.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TokenJPAService implements TokenService{

    private final TokenRepository tokenRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public TokenJPAService( TokenRepository tokenRepository, RequestRepository requestRepository ){
        this.tokenRepository = tokenRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public Token getTokenById( Integer id ){
        if( id == null )
            throw new InvalidTokenIdentifierException();
        return tokenRepository.findOne( id );
    }

    @Override
    public Token getToken( String token ){
        return tokenRepository.findByToken( token );
    }

    @Override
    public List<Token> getTokensForCard( Card card ){
        return tokenRepository.findByCard( card );
    }

    @Override
    public List<Request> getRequestsFromToken( int id ){
        return requestRepository.findByToken( this.getTokenById( id ) );
    }

    @Override
    public Token createToken( Card card, String client ){
        Token token = new Token();

        token.setCard( card );
        token.setToken( UUID.randomUUID().toString() );
        token.setClient( client );
        token.setCreationDate( DateService.now() );
        token.setExpirationDate( DateService.now().plusDays( Config.DEFAULT_TOKEN_TTL ) );
        token.setStatus( TokenStatus.ACTIVE );

        return tokenRepository.save( token );
    }

    @Override
    public Request createRequest( String method, String endpoint, String ip, Token token ){
        Request request = new Request();

        request.setMethod( method );
        request.setEndpoint( endpoint );
        request.setIp( ip );
        request.setTimestamp( DateService.now() );
        request.setToken( token );

        return requestRepository.save( request );
    }

    @Override
    public Token extendToken( int id ){
        Token token = this.getTokenById( id );

        token.setExpirationDate( DateService.now().plusDays( Config.DEFAULT_TOKEN_TTL ) );

        return tokenRepository.save( token );
    }

    @Override
    public void deactivateToken( int id ){
        Token token = this.getTokenById( id );

        token.setStatus( TokenStatus.INACTIVE );

        tokenRepository.save( token );
    }

    @Override
    public void deactivateAllTokensForCard( Card card ){
        List<Token> tokens = tokenRepository.findByCard( card );

        if( tokens != null )
            for( Token token : tokens ){
                token.setStatus( TokenStatus.INACTIVE );
                tokenRepository.save( token );
            }
    }
}
