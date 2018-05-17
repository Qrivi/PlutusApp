package be.plutus.api.resource;

import be.plutus.core.model.Token;
import be.plutus.core.service.CardService;
import be.plutus.core.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(
        path = "/auth",
        produces = MediaType.APPLICATION_JSON_VALUE )
public class AuthResource{

    private final TokenService tokenService;

    private final CardService cardService;

    private final be.plutus.api.util.service.MessageService messageService;

    private final be.plutus.api.util.mapper.TokenMapper tokenMapper;

    @Autowired
    public AuthResource( TokenService tokenService, CardService cardService, be.plutus.api.util.service.MessageService messageService, be.plutus.api.util.mapper.TokenMapper tokenMapper ){
        this.tokenService = tokenService;
        this.cardService = cardService;
        this.messageService = messageService;
        this.tokenMapper = tokenMapper;
    }

    // region GET

    @RequestMapping( value = "/refresh",
            method = RequestMethod.GET )
    public ResponseEntity<Response> refresh( HttpServletRequest request ){

        be.plutus.api.response.Response.Builder response = new be.plutus.api.response.Response.Builder();

        Token token = tokenService.extendToken( tokenService.getToken( request.getHeader( "X-Auth-Token" ) ).getId() );

        response.meta( be.plutus.api.response.Meta.success() );
        response.data( tokenMapper.map( token ) );

        return new ResponseEntity<>( response.build(), HttpStatus.OK );
    }

    // endregion

    //region POST

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> post( @Valid @RequestBody be.plutus.api.dto.request.AuthenticationDTO dto, BindingResult result, HttpServletRequest request ){

        if( result.hasErrors() )
            return be.plutus.api.resource.util.ResourceUtils.createErrorResponse( result );

        User user = cardService.getUserByUsername( dto.getCardNumber() );

        be.plutus.api.response.Response.Builder response = new be.plutus.api.response.Response.Builder();

        if( user == null ){
            response.meta( be.plutus.api.response.Meta.badRequest() );
            response.errors( messageService.get( "NotValid.AuthResource.username" ) );
            return new ResponseEntity<>( response.build(), HttpStatus.BAD_REQUEST );
        }

        if( !user.isPasswordValid( dto.getPassword() ) ){
            response.meta( be.plutus.api.response.Meta.badRequest() );
            response.errors( messageService.get( "NotValid.AuthResource.password" ) );
            return new ResponseEntity<>( response.build(), HttpStatus.BAD_REQUEST );
        }

        Token token = tokenService.createToken( user, dto.getClient(), dto.getDevice(), request.getRemoteAddr() );

        cardService.updateUserLastLogin( user.getId() );

        response.meta( be.plutus.api.response.Meta.success() );
        response.data( tokenMapper.map( token ) );

        return new ResponseEntity<>( response.build(), HttpStatus.OK );
    }

    //endregion

    // region DELETE
    
    @RequestMapping( method = RequestMethod.DELETE )
    public ResponseEntity<Response> delete( HttpServletRequest request ){

        tokenService.deactivateToken( tokenService.getToken( request.getHeader( "X-Auth-Token" ) ).getId() );

        Response response = new be.plutus.api.response.Response.Builder()
                .meta( be.plutus.api.response.Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    // endregion
}