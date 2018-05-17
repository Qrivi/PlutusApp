package be.plutus.api.resource;


import be.plutus.api.dto.request.MenuTemplateCreateDTO;
import be.plutus.api.dto.request.MenuTemplateRemoveRequirementsDTO;
import be.plutus.api.dto.request.MenuTemplateUpdateDTO;
import be.plutus.api.dto.request.MenuTemplateUpdateRequirementsDTO;
import be.plutus.api.dto.response.comparator.MenuTemplateNameComparator;
import be.plutus.api.resource.util.ResourceUtils;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.util.mapper.MenuTemplateMapper;
import be.plutus.api.util.service.MessageService;
import be.plutus.core.model.MenuTemplate;
import be.plutus.core.service.MenuTemplateService;
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
        path = "/menutemplate",
        produces = MediaType.APPLICATION_JSON_VALUE )
public class MenuTemplateResource{

    private final MessageService messageService;

    private final MenuTemplateService service;
    private final MenuTemplateMapper mapper;

    @Autowired
    public MenuTemplateResource( MessageService messageService, MenuTemplateService service, MenuTemplateMapper mapper ){
        this.messageService = messageService;
        this.service = service;
        this.mapper = mapper;
    }

    //region GET

    /**
     * @api {get} /menutemplate Get existing templates
     * @apiDescription Fetches existing menu templates and returns their details. If a strict filter is used (e.g
     * <code>name</code>), <code>data</code> will consist of a JSON object rather than a JSON array.
     * @apiUse getMenuTemplateDTOs
     * @apiGroup MenuTemplate
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
            filteredResult = mapper.map( service.getMenuTemplateByName( name ) );

        }else if( like != null ){
            filteredResult = service.getAllMenuTemplatesLike( like )
                    .stream()
                    .map( mapper::map )
                    .sorted( new MenuTemplateNameComparator() )
                    .collect( Collectors.toList() );

        }else{
            filteredResult = service.getAllMenuTemplates()
                    .stream()
                    .map( mapper::map )
                    .sorted( new MenuTemplateNameComparator() )
                    .collect( Collectors.toList() );
        }

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( filteredResult )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /menutemplate/{id} Get a specific template
     * @apiDescription Fetches an existing menu template by its <code>id</code> and returns its details.
     * @apiUse getMenuTemplateDTO
     * @apiGroup MenuTemplate
     * @apiName getById
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getById( @PathVariable( name = "id" ) Integer id ){

        MenuTemplate menuTemplate = service.getMenuTemplate( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.map( menuTemplate ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /menutemplate/{id}/requirements Get a template's requirements
     * @apiDescription Fetches an existing menu template by its <code>id</code> and returns its details and a list of
     * requirements that belong to the menu template.
     * @apiUse getMenuTemplateRequirementsDTO
     * @apiGroup MenuTemplate
     * @apiName getByIdWithRequirements
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}/requirements",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getByIdWithRequirements( @PathVariable( name = "id" ) Integer id ){

        MenuTemplate menuTemplate = service.getMenuTemplate( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.mapWithRequirements( menuTemplate ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region POST

    /**
     * @api {post} /menutemplate Create a new template
     * @apiDescription Creates a new menu template of the submitted values and returns its details.
     * @apiUse createMenuTemplateDTO
     * @apiUse getMenuTemplateDTO
     * @apiGroup MenuTemplate
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
    public ResponseEntity<Response> post( @Valid @RequestBody MenuTemplateCreateDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        MenuTemplate menuTemplate = service.createMenuTemplate( dto.getName(), dto.getDescription(), dto.getPrice(), dto.getImagePath() );

        Response response = new Response.Builder()
                .meta( Meta.created() )
                .data( mapper.map( menuTemplate ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    //endregion

    //region PUT

    /**
     * @api {put} /menutemplate/{id} Update a specific template
     * @apiDescription Updates an existing menu template with the submitted values.
     * @apiUse updateMenuTemplateDTO
     * @apiGroup MenuTemplate
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
    public ResponseEntity<Response> update( @PathVariable( name = "id" ) Integer id, @Valid @RequestBody MenuTemplateUpdateDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.updateMenuTemplate( id, dto.getName(), dto.getDescription(), dto.getPrice(), dto.getImagePath() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {put} /menutemplate/{id}/requirements Update a template's requirements
     * @apiDescription Looks up products and groups with the <code>id</code>s specified in the request body, and adds
     * them to the template if they exist.
     * @apiUse updateMenuTemplateGroupsDTO
     * @apiGroup MenuTemplate
     * @apiName updateGroups
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/{id}/requirements",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateRequirements( @PathVariable( name = "id" ) Integer id, @RequestBody MenuTemplateUpdateRequirementsDTO dto ){

        if( dto.getRequiredProducts() == null && dto.getOptionalProducts() == null
                && dto.getRequiredGroups() == null && dto.getOptionalGroups() == null ){

            Response response = new Response.Builder()
                    .meta( Meta.badRequest() )
                    .errors( messageService.get( "BadRequest.MenuTemplateResource.updateGroups" ) )
                    .build();
            return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
        }else{
            if( dto.getRequiredProducts() != null )
                service.addRequiredProductsToMenuTemplate( id, dto.getRequiredProducts() );
            if( dto.getOptionalProducts() != null )
                service.addOptionalProductsToMenuTemplate( id, dto.getOptionalProducts() );
            if( dto.getRequiredGroups() != null )
                service.addRequiredGroupsToMenuTemplate( id, dto.getRequiredGroups() );
            if( dto.getOptionalGroups() != null )
                service.addOptionalGroupsToMenuTemplate( id, dto.getOptionalGroups() );
        }
        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region DELETE

    /**
     * @api {delete} /menutemplate/{id} Delete a specific template
     * @apiDescription Deletes an existing menu template matching <code>id</code>.
     * @apiGroup MenuTemplate
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

        service.removeMenuTemplate( id );

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {delete} /menutemplate/{id}/groups Remove a template's requirements
     * @apiDescription Looks up products and groups with the <code>id</code>s specified in the request body, and
     * removes them to the template if they exist.
     * @apiGroup MenuTemplate
     * @apiName deleteGroups
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( value = "/{id}/requirements",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> deleteGroups( @PathVariable( name = "id" ) Integer id, @RequestBody MenuTemplateRemoveRequirementsDTO dto ){

        if( dto.getRequiredProducts() == null && dto.getOptionalProducts() == null
                && dto.getRequiredGroups() == null && dto.getOptionalGroups() == null ){

            service.removeAllProductsAndAllGroupsFromMenuTemplate( id );
        }else{
            if( dto.getRequiredProducts() != null )
                service.removeRequiredProductsFromMenuTemplate( id, dto.getRequiredProducts() );
            if( dto.getOptionalProducts() != null )
                service.removeOptionalProductsFromMenuTemplate( id, dto.getOptionalProducts() );
            if( dto.getRequiredGroups() != null )
                service.removeRequiredGroupsFromMenuTemplate( id, dto.getRequiredGroups() );
            if( dto.getOptionalGroups() != null )
                service.removeOptionalGroupsFromMenuTemplate( id, dto.getOptionalGroups() );
        }
        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    // endregion
}