package be.plutus.core.service;

import be.plutus.core.model.Card;
import be.plutus.core.model.Request;
import be.plutus.core.model.Token;

import java.util.List;

public interface TokenService{

    Token getTokenById( Integer id );

    Token getToken( String token );

    List<Token> getTokensForCard( Card card );

    List<Request> getRequestsFromToken( int id );

    Token createToken( Card card, String client );

    Request createRequest( String method, String endpoint, String ip, Token token );

    Token extendToken( int id );

    void deactivateToken( int id );

    void deactivateAllTokensForCard( Card card );
}
