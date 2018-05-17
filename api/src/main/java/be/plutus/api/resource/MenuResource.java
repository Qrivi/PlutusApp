package be.plutus.api.resource;


import be.plutus.api.dto.request.MenuCreateDTO;
import be.plutus.api.dto.request.MenuRemoveProductsDTO;
import be.plutus.api.dto.request.MenuUpdateDTO;
import be.plutus.api.dto.request.MenuUpdateProductsDTO;
import be.plutus.api.dto.response.comparator.MenuNameComparator;
import be.plutus.api.resource.util.ResourceUtils;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.util.mapper.MenuMapper;
import be.plutus.core.model.orderable.Menu;
import be.plutus.core.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        path = "/menu",
        produces = MediaType.APPLICATION_JSON_VALUE )
public class MenuResource{

    private final MenuService service;
    private final MenuMapper mapper;

    @Autowired
    public MenuResource( MenuService service, MenuMapper mapper ){
        this.service = service;
        this.mapper = mapper;
    }

    //region GET

    /**
     * @api {get} /menu Get existing menus
     * @apiDescription Fetches existing menus and returns their details.
     * @apiUse getMenuDTOs
     * @apiGroup Menu
     * @apiName get
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( method = RequestMethod.GET )
    public ResponseEntity<Response> get(){

        List<Menu> menus = service.getAllMenus();

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( menus.stream()
                        .map( mapper::map )
                        .sorted( new MenuNameComparator() )
                        .collect( Collectors.toList() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /menu/{id} Get a specific menu
     * @apiDescription Fetches an existing menu by its <code>id</code> and returns its details.
     * @apiUse getMenuDTO
     * @apiGroup Menu
     * @apiName getById
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getById( @PathVariable( name = "id" ) Integer id ){

        Menu menu = service.getMenu( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.map( menu ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /menu/{id}/products Get a group's products
     * @apiDescription Fetches an existing menu by its <code>id</code> and returns its details and a list of products
     * that belong to the menu template.
     * @apiUse getMenuProductsDTO
     * @apiGroup Menu
     * @apiName getProducts
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}/products",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getProducts( @PathVariable( name = "id" ) Integer id ){

        Menu menu = service.getMenu( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.mapWithProducts( menu ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region POST

    /**
     * @api {post} /menu Create a new menu
     * @apiDescription Creates a new menu of the submitted values and returns its details.
     * @apiUse createMenuDTO
     * @apiUse getMenuDTO
     * @apiGroup Menu
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
    public ResponseEntity<Response> post( @Valid @RequestBody MenuCreateDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        Menu menu = service.createMenu( dto.getTemplateId() );

        Response response = new Response.Builder()
                .meta( Meta.created() )
                .data( mapper.map( menu ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    //endregion

    //region PUT
    
    /**
     * @api {put} /menu/{id} Update a specific menu
     * @apiDescription Updates an existing group with the submitted values.
     * @apiUse updateMenuDTO
     * @apiGroup Menu
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
    public ResponseEntity<Response> update( @PathVariable( name = "id" ) Integer id, @Valid @RequestBody MenuUpdateDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.updateMenu( id, dto.getTemplateId() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {put} /menu/{id}/products Update a menu's products
     * @apiDescription Looks up products with the <code>id</code>s specified in the request body, and adds them to the
     * group if they exist.
     * @apiUse updateMenuProductsDTO
     * @apiGroup Menu
     * @apiName updateProducts
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}/products",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateProducts( @PathVariable( name = "id" ) Integer id, @Valid @RequestBody MenuUpdateProductsDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.addProductsToMenu( id, dto.getProducts() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region DELETE

    /**
     * @api {delete} /menu/{id} Delete a specific menu
     * @apiDescription Deletes an existing group matching <code>id</code>.
     * @apiGroup Menu
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

        service.removeMenu( id );

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {delete} /menu/{id}/products Remove a menu's products
     * @apiDescription Looks up products with the <code>id</code>s specified in the request body, and removes them from
     * the group if they exist.
     * @apiGroup Menu
     * @apiName deleteProducts
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}/products",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> deleteProducts( @PathVariable( name = "id" ) Integer id, @RequestBody MenuRemoveProductsDTO dto ){

        if( dto.getProducts() == null )
            service.removeAllProductsFromMenu( id );
        else
            service.removeProductsFromMenu( id, dto.getProducts() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    // endregion
}