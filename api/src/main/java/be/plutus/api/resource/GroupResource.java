package be.plutus.api.resource;


import be.plutus.api.dto.request.GroupCreateDTO;
import be.plutus.api.dto.request.GroupRemoveProductsDTO;
import be.plutus.api.dto.request.GroupUpdateDTO;
import be.plutus.api.dto.request.GroupUpdateProductsDTO;
import be.plutus.api.dto.response.comparator.GroupNameComparator;
import be.plutus.api.resource.util.ResourceUtils;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.util.mapper.GroupMapper;
import be.plutus.core.model.Group;
import be.plutus.core.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        path = "/group",
        produces = MediaType.APPLICATION_JSON_VALUE )
public class GroupResource{

    private final GroupService service;
    private final GroupMapper mapper;

    @Autowired
    public GroupResource( GroupService service, GroupMapper mapper ){
        this.service = service;
        this.mapper = mapper;
    }

    //region GET

    /**
     * @api {get} /group Get existing groups
     * @apiDescription Fetches existing groups and returns their details. If a strict filter is used (e.g
     * <code>name</code>), <code>data</code> will consist of a JSON object rather than a JSON array.
     * @apiUse getGroupDTOs
     * @apiGroup Group
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
            filteredResult = mapper.map( service.getGroupByName( name ) );

        }else if( like != null ){
            filteredResult = service.getAllGroupsLike( like )
                    .stream()
                    .map( mapper::map )
                    .sorted( new GroupNameComparator() )
                    .collect( Collectors.toList() );

        }else{
            filteredResult = service.getAllGroups()
                    .stream()
                    .map( mapper::map )
                    .sorted( new GroupNameComparator() )
                    .collect( Collectors.toList() );
        }

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( filteredResult )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /group/{id} Get a specific group
     * @apiDescription Fetches an existing group by its <code>id</code> and returns its details.
     * @apiUse getGroupDTO
     * @apiGroup Group
     * @apiName getById
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getById( @PathVariable( name = "id" ) Integer id ){

        Group group = service.getGroup( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.map( group ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /group/{id}/products Get a group's products
     * @apiDescription Fetches an existing group by its <code>id</code> and returns its details and a list of products
     * that belong to the group.
     * @apiUse getGroupWithProductsDTO
     * @apiGroup Group
     * @apiName getByIdWithProducts
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}/products",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getByIdWithProducts( @PathVariable( name = "id" ) Integer id ){

        Group group = service.getGroup( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.mapWithProducts( group ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region POST

    /**
     * @api {post} /group Create a new group
     * @apiDescription Creates a new group of the submitted values and returns its details.
     * @apiUse createGroupDTO
     * @apiUse getGroupDTO
     * @apiGroup Group
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
    public ResponseEntity<Response> post( @RequestBody GroupCreateDTO dto ){

        Group group = service.createGroup( dto.getName(), dto.getDescription(), dto.getImagePath(), dto.getSelectable() );

        Response response = new Response.Builder()
                .meta( Meta.created() )
                .data( mapper.map( group ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    //endregion

    //region PUT

    /**
     * @api {put} /group/{id} Update a specific group
     * @apiDescription Updates an existing group with the submitted values.
     * @apiUse updateGroupDTO
     * @apiGroup Group
     * @apiName update
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
    public ResponseEntity<Response> update( @PathVariable( name = "id" ) Integer id, @RequestBody GroupUpdateDTO dto ){

        service.updateGroup( id, dto.getName(), dto.getDescription(), dto.getImagePath(), dto.getSelectable() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {put} /group/{id}/products Update a group's products
     * @apiDescription Looks up products with the <code>id</code>s specified in the request body, and adds them to the
     * group if they exist.
     * @apiUse updateGroupProductsDTO
     * @apiGroup Group
     * @apiName updateProducts
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/{id}/products",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateProducts( @PathVariable( name = "id" ) Integer id, @Valid @RequestBody GroupUpdateProductsDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.addProductsToGroup( id, dto.getProducts() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region DELETE

    /**
     * @api {delete} /group/{id} Delete a specific group
     * @apiDescription Deletes an existing group matching <code>id</code>.
     * @apiGroup Group
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

        service.removeGroup( id );

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {delete} /group/{id}/products Remove a group's products
     * @apiDescription Looks up products with the <code>id</code>s specified in the request body, and removes them from
     * the group if they exist.
     * @apiGroup Group
     * @apiName deleteProducts
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/{id}/products",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> deleteProducts( @PathVariable( name = "id" ) Integer id, @RequestBody GroupRemoveProductsDTO dto ){

        if( dto.getProducts() == null )
            service.removeAllProductsFromGroup( id );
        else
            service.removeProductsFromGroup( id, dto.getProducts() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    // endregion
}