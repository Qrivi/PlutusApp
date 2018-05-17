package be.plutus.api.resource;

import be.plutus.api.dto.request.*;
import be.plutus.api.dto.response.comparator.UserUsernameComparator;
import be.plutus.api.resource.util.ResourceUtils;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.security.context.SecurityContext;
import be.plutus.api.util.mapper.UserMapper;
import be.plutus.api.util.service.MessageService;
import be.plutus.core.config.Config;
import be.plutus.core.model.user.User;
import be.plutus.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        path = "/user",
        produces = MediaType.APPLICATION_JSON_VALUE )
public class UserResource{

    private final MessageService messageService;

    private final UserService service;
    private final UserMapper mapper;

    @Autowired
    public UserResource( MessageService messageService, UserService service, UserMapper mapper ){
        this.messageService = messageService;
        this.service = service;
        this.mapper = mapper;
    }

    // region GET

    /**
     * @api {get} /user Get existing user
     * @apiDescription Fetches existing users and returns their details. If a strict filter is used (e.g
     * <code>name</code>), <code>data</code> will consist of a JSON object rather than a JSON array.
     * @apiUse getUserDTOs
     * @apiGroup User
     * @apiName get
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( method = RequestMethod.GET )
    public ResponseEntity<Response> get(
            @RequestParam( value = "name", required = false ) String name,
            @RequestParam( value = "like", required = false ) String like ){

        Object filteredResult;

        if( name != null ){
            filteredResult = mapper.map( service.getUserByUsername( name ) );

        }else if( like != null ){
            filteredResult = service.getAllUsersLike( like )
                    .stream()
                    .map( mapper::map )
                    .sorted( new UserUsernameComparator() )
                    .collect( Collectors.toList() );

        }else{
            filteredResult = service.getAllUsers()
                    .stream()
                    .map( mapper::map )
                    .sorted( new UserUsernameComparator() )
                    .collect( Collectors.toList() );
        }

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( filteredResult )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /user/{id} Get a specific user
     * @apiDescription Fetches an existing user by its <code>id</code> and returns its details.
     * @apiUse getUserDTO
     * @apiGroup User
     * @apiName getById
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/{id}",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getById( @PathVariable( name = "id" ) Integer id ){

        User user = service.getUser( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.map( user ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @RequestMapping( value = "/self",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getSelf(){

        return getById( SecurityContext.getUser().getId() );
    }

    /**
     * @api {get} /user/self/sequence Get a user's sequence
     * @apiDescription Fetches the current user and returns its details and a list of item <code>id</code>s that
     * represent the preferred order in which items should be listed for that user.
     * @apiUse getUserPreferredOrderDTO
     * @apiGroup User
     * @apiName getItemSequence
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/self/sequence",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getItemSequence(){

        User user = service.getUser( SecurityContext.getUser().getId() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.mapWithPreferredOrder( user ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region POST

    /**
     * @api {post} /user Create a new user
     * @apiDescription Creates a new user of the submitted values and returns its details.
     * @apiUse createUserDTO
     * @apiUse getUserDTO
     * @apiGroup User
     * @apiName post
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> post( @Valid @RequestBody UserCreateDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        User user = service.createUser( dto.getUsername(), dto.getPassword(), dto.getFirstName(), dto.getLastName(), dto.getRole(), dto.getLanguage(), dto.getImagePath() );

        Response response = new Response.Builder()
                .meta( Meta.created() )
                .data( mapper.map( user ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    //endregion

    //region PUT
    /**
     * @api {put} /user/{id} Update a specific user
     * @apiDescription Updates an existing user with the submitted values.
     * @apiUse updateUserDTO
     * @apiGroup User
     * @apiName updateUser
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateUser( @PathVariable( name = "id" ) Integer id, @Valid @RequestBody UserUpdateDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.updateUser( id, dto.getUsername(), null, dto.getFirstName(), dto.getLastName(), null, dto.getLanguage(), dto.getImagePath() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @RequestMapping( value = "/self",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateSelf( @Valid @RequestBody UserUpdateDTO dto, BindingResult result ){

        return updateUser( SecurityContext.getUser().getId(), dto, result );
    }

    /**
     * @api {put} /user/self/password Update a user's password
     * @apiDescription Updates the current user's password with the submitted value.
     * @apiUse updateUserPasswordDTO
     * @apiGroup User
     * @apiName updateOwnPassword
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/self/password",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateOwnPassword( @Valid @RequestBody UserUpdatePasswordDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.updateUser( SecurityContext.getUser().getId(), null, dto.getPassword(), null, null, null, null, null );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {put} /user/{id}/password Reset a user's password
     * @apiDescription Resets an existing user's password to the default password, which is <code>FrittoUser</code>.
     * @apiGroup User
     * @apiName resetPassword
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/{id}/password",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> resetPassword( @PathVariable( name = "id" ) Integer id){

        service.updateUser( id, null, Config.DEFAULT_USER_PASSWORD, null, null, null, null, null );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {put} /user/{id}/role Update user's role
     * @apiDescription Updates an existing user's role with the submitted value.
     * @apiUse updateUserRoleDTO
     * @apiGroup User
     * @apiName updateRole
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/{id}/role",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateRole( @PathVariable( name = "id" ) Integer id, @Valid @RequestBody UserUpdateRoleDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.updateUser( id, null, null, null, null, dto.getRole(), null, null );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/self/role",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateOwnRole( @Valid @RequestBody UserUpdateRoleDTO dto, BindingResult result ){

        return updateRole( SecurityContext.getUser().getId(), dto, result );
    }

    /**
     * @api {put} /user/self/sequence Update a user's sequence
     * @apiDescription Sets the current user's preferred order in which items should be listed to the list of item
     * <code>id</code>s submitted. This will completely overwrite their current preference.
     * @apiUse updateUserPreferredOrderDTO
     * @apiGroup User
     * @apiName updateItemSequence
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/self/sequence",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateItemSequence( @RequestBody UserUpdatePreferredOrderDTO dto ){

        service.updateUserItemSequence( SecurityContext.getUser().getId(), dto.getItemOrder() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region DELETE

    /**
     * @api {delete} /user/{id} Delete a specific user
     * @apiDescription Deletes an existing user matching <code>id</code>.
     * @apiGroup User
     * @apiName delete
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/{id}",
            method = RequestMethod.DELETE )
    public ResponseEntity<Response> delete( @PathVariable( name = "id" ) Integer id ){

        if( service.getUser( id ) == null )
            return ResourceUtils.createNotFoundResponse();

        if( Objects.equals( id, SecurityContext.getUser().getId() ) ){

            Response response = new Response.Builder()
                    .meta( Meta.forbidden() )
                    .errors( messageService.get( "Forbidden.UserResource.delete" ) )
                    .build();

            return new ResponseEntity<>( response, HttpStatus.FORBIDDEN );
        }

        service.removeUser( id );

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {delete} /user/self/sequence Remove a user's sequence
     * @apiDescription Removes the current user's preferred order in which items should be listed. Until a new preferred
     * order is set, items will be ordered using the default sorting algorithm, which is alphabetically by name.
     * @apiGroup User
     * @apiName deleteItemSequence
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/self/sequence",
            method = RequestMethod.DELETE )
    public ResponseEntity<Response> deleteItemSequence(){

        service.updateUserItemSequence( SecurityContext.getUser().getId(), null );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {delete} /user/self/order Remove a user's current order
     * @apiDescription Clears the current user's current order, so a new order can be started safely.
     * @apiGroup User
     * @apiName deleteOrder
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {String} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/self/order",
            method = RequestMethod.DELETE )
    public ResponseEntity<Response> deleteOrder(){

        service.updateUserCurrentOrder( SecurityContext.getUser().getId(), null );

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion
}

