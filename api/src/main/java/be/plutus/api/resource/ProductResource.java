package be.plutus.api.resource;


import be.plutus.api.dto.request.ProductCreateDTO;
import be.plutus.api.dto.request.ProductUpdateDTO;
import be.plutus.api.dto.response.comparator.ProductNameComparator;
import be.plutus.api.resource.util.ResourceUtils;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.util.mapper.ProductMapper;
import be.plutus.core.model.orderable.Product;
import be.plutus.core.service.ProductService;
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
        path = "/product",
        produces = MediaType.APPLICATION_JSON_VALUE )
public class ProductResource{

    private final ProductService service;
    private final ProductMapper mapper;

    @Autowired
    public ProductResource( ProductService service, ProductMapper mapper ){
        this.service = service;
        this.mapper = mapper;
    }

    //region GET

    /**
     * @api {get} /product Get existing products
     * @apiDescription Fetches existing products and returns their details. If a strict filter is used (e.g
     * <code>name</code>), <code>data</code> will consist of a JSON object rather than a JSON array.
     * @apiUse getProductDTOs
     * @apiGroup Product
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
            filteredResult = mapper.map( service.getProductByName( name ) );

        }else if( like != null ){
            filteredResult = service.getAllProductsLike( like )
                    .stream()
                    .map( mapper::map )
                    .sorted( new ProductNameComparator() )
                    .collect( Collectors.toList() );

        }else{
            filteredResult = service.getAllProducts()
                    .stream()
                    .map( mapper::map )
                    .sorted( new ProductNameComparator() )
                    .collect( Collectors.toList() );
        }

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( filteredResult )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }
    
    /**
     * @api {get} /product/{id} Get a specific product
     * @apiDescription Fetches an existing product by its <code>id</code> and returns its details.
     * @apiUse getProductDTO
     * @apiGroup Product
     * @apiName getById
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getById( @PathVariable( name = "id" ) Integer id ){

        Product product = service.getProduct( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.map( product ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region POST

    /**
     * @api {post} /product Create a new product
     * @apiDescription Creates a new product of the submitted values and returns its details.
     * @apiUse createProductDTO
     * @apiUse getProductDTO
     * @apiGroup Product
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
    public ResponseEntity<Response> post( @Valid @RequestBody ProductCreateDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        Product product = service.createProduct( dto.getName(), dto.getDescription(), dto.getPrice(), dto.getImagePath(), dto.getSelectable() );

        Response response = new Response.Builder()
                .meta( Meta.created() )
                .data( mapper.map( product ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    //endregion

    //region PUT
    /**
     * @api {put} /product/{id} Update a specific product
     * @apiDescription Updates an existing group with the submitted values.
     * @apiUse updateProductDTO
     * @apiGroup Product
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
    public ResponseEntity<Response> update( @PathVariable( name = "id" ) Integer id, @Valid @RequestBody ProductUpdateDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.updateProduct( id, dto.getName(), dto.getDescription(), dto.getPrice(), dto.getImagePath(), dto.getSelectable() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region DELETE

    /**
     * @api {delete} /product/{id} Delete a specific product
     * @apiDescription Deletes an existing product matching <code>id</code>.
     * @apiGroup Product
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

        service.removeProduct( id );

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    // endregion
}