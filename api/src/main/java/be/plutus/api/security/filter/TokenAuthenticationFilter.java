package be.plutus.api.security.filter;

import be.plutus.api.exception.*;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.util.service.MessageService;
import be.plutus.common.DateService;
import be.plutus.core.model.Card;
import be.plutus.core.model.Token;
import be.plutus.core.model.TokenStatus;
import be.plutus.core.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends GenericFilterBean{

    private static String PARAMETER_SECURITY_TOKEN = "token";
    private static String HEADER_SECURITY_TOKEN = "X-Auth-Token";

    private TokenService tokenService;
    private ObjectMapper objectMapper;
    private MessageService messageService;

    public TokenAuthenticationFilter( TokenService tokenService, ObjectMapper objectMapper, MessageService messageService ){
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
        this.messageService = messageService;
    }

    @Override
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException, ServletException{
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;

        try{
            String tokenHeader = request.getHeader( HEADER_SECURITY_TOKEN );
            String tokenParameter = request.getParameter( PARAMETER_SECURITY_TOKEN );

            if( SecurityContextHolder.getContext().getAuthentication() == null ){
                Token token = null;
                if( tokenHeader == null && tokenParameter == null )
                    throw new TokenNotFoundException();
                if( tokenHeader != null )
                    token = tokenService.getToken( tokenHeader );
                if( tokenParameter != null )
                    token = tokenService.getToken( tokenParameter );

                if( isValid( token ) ){
                    tokenService.createRequest( request.getServletPath(), request.getMethod(), request.getRemoteAddr(), token );

                    Card card = token.getCard();

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    new UsernamePasswordAuthenticationToken(
                                            card,
                                            null,
                                            null
                                    )
                            );
                }
            }
            chain.doFilter( request, response );

        }catch(   AuthenticationException e ){
            Meta.Builder meta = new Meta.Builder();
            meta = ( e.getStatus() == 403 ) ? meta.forbidden() : meta.unauthorized();

            Response response2 = new Response.Builder()
                    .meta( meta.build() )
                    .errors( messageService.get( e.getClass().getName() ) )
                    .build();

            response.setContentType( "application/json" );
            response.setStatus( e.getStatus() );

            objectMapper.writeValue( res.getOutputStream(), response2 );
        }
    }

    private boolean isValid( Token token ){
        if( token == null )
            throw new TokenInvalidException();
        if( token.getStatus() == TokenStatus.INACTIVE )
            throw new TokenNotActiveException();
        if( token.getExpirationDate().isBefore( DateService.now() ) )
            throw new TokenExpiredException();
        return true;
    }
}
