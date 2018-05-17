package be.plutus.api.resource;

import be.plutus.api.dto.request.ClientCreateDTO;
import be.plutus.api.dto.request.ClientUpdateDTO;
import be.plutus.api.dto.response.comparator.ClientUsernameComparator;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.security.context.SecurityContext;
import be.plutus.api.util.mapper.ClientMapper;
import be.plutus.api.util.service.MessageService;
import be.plutus.core.model.order.Client;
import be.plutus.core.service.ClientService;
import be.plutus.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        path = "/client",
        produces = MediaType.APPLICATION_JSON_VALUE )
public class ClientResource{

    private final MessageService messageService;

    private final ClientService service;
    private final ClientMapper mapper;

    @Autowired
    public ClientResource( MessageService messageService, ClientService service, ClientMapper mapper ){
        this.messageService = messageService;
        this.service = service;
        this.mapper = mapper;
    }

    //region GET

    /**
     * @api {get} /client Get existing clients
     * @apiDescription Fetches existing clients and returns their details. If a strict filter is used (e.g
     * <code>name</code>), <code>data</code> will consist of a JSON object rather than a JSON array.
     * @apiUse getClientDTOs
     * @apiGroup Client
     * @apiName get
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( method = RequestMethod.GET )
    public ResponseEntity<Response> get(
            @RequestParam( value = "name", required = false ) String name,
            @RequestParam( value = "like", required = false ) String like ){

        Object filteredResult;

        if( name != null ){
            filteredResult = mapper.map( service.getClientByUsername( name ) );

        }else if( like != null ){
            filteredResult = service.getAllClientsLike( like )
                    .stream()
                    .map( mapper::map )
                    .sorted( new ClientUsernameComparator() )
                    .collect( Collectors.toList() );

        }else{
            filteredResult = service.getAllClients()
                    .stream()
                    .map( mapper::map )
                    .sorted( new ClientUsernameComparator() )
                    .collect( Collectors.toList() );
        }

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( filteredResult )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /client/{id} Get a specific client
     * @apiDescription Fetches an existing client by its <code>id</code> and returns its details.
     * @apiUse getClientDTO
     * @apiGroup Client
     * @apiName getById
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getById( @PathVariable( name = "id" ) Integer id ){

        Client client = service.getClient( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.map( client ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region POST

    /**
     * @api {post} /client Create a new client
     * @apiDescription Creates a new client of the submitted values and returns its details.
     * @apiUse createClientDTO
     * @apiUse getClientDTO
     * @apiGroup Client
     * @apiName post
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> post( @RequestBody ClientCreateDTO dto ){

        Client client = service.createClient( dto.getUsername(), dto.getFirstName(), dto.getLastName() );

        Response response = new Response.Builder()
                .meta( Meta.created() )
                .data( mapper.map( client ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    //endregion

    //region PUT

    /**
     * @api {put} /client/{id} Update a specific client
     * @apiDescription Updates an existing client with the submitted values.
     * @apiUse updateClientDTO
     * @apiGroup Client
     * @apiName update
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> update( @PathVariable( name = "id" ) Integer id, @RequestBody ClientUpdateDTO dto ){

        service.updateClient( id, dto.getUsername(), dto.getFirstName(), dto.getLastName() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region DELETE

    /**
     * @api {delete} /client/{id} Delete a specific client
     * @apiDescription Deletes an existing client matching <code>id</code>.
     * @apiGroup Client
     * @apiName delete
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}", method = RequestMethod.DELETE )
    public ResponseEntity<Response> delete( @PathVariable( name = "id" ) Integer id ){

        service.removeClient( id );

        if( Objects.equals( id, SecurityContext.getUser().getId() ) ){

            Response response = new Response.Builder()
                    .meta( Meta.forbidden() )
                    .errors( messageService.get( "Forbidden.ClientResource.delete" ) )
                    .build();

            return new ResponseEntity<>( response, HttpStatus.FORBIDDEN );
        }

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    // endregion
}

