package be.plutus.api.resource;


import be.plutus.api.dto.request.OrderCreateDTO;
import be.plutus.api.dto.request.OrderRemoveItemsDTO;
import be.plutus.api.dto.request.OrderUpdateDTO;
import be.plutus.api.dto.request.OrderUpdateItemsDTO;
import be.plutus.api.dto.response.OrderDTO;
import be.plutus.api.dto.response.comparator.OrderDateComparator;
import be.plutus.api.exception.IllegalParameterException;
import be.plutus.api.resource.util.ResourceUtils;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.security.context.SecurityContext;
import be.plutus.api.util.mapper.OrderMapper;
import be.plutus.core.model.order.Order;
import be.plutus.core.model.order.OrderStatus;
import be.plutus.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        path = "/order",
        produces = MediaType.APPLICATION_JSON_VALUE )
public class OrderResource{

    private final OrderService service;
    private final OrderMapper mapper;

    @Autowired
    public OrderResource( OrderService service, OrderMapper mapper ){
        this.service = service;
        this.mapper = mapper;
    }

    //region GET

    /**
     * @api {get} /order Get existing orders
     * @apiDescription Fetches existing orders and returns their details.
     * @apiUse getOrderDTOs
     * @apiGroup Order
     * @apiName get
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( method = RequestMethod.GET )
    public ResponseEntity<Response> get(
            @RequestParam( value = "status", required = false ) String status,
            @RequestParam( value = "client", required = false ) String client ){

        Object filteredResult;

        if( client != null && status != null ){
            filteredResult = this.getOrdersByClientId( client )
                    .retainAll( this.getOrdersByOrderStatus( status ) );

        }else if( client != null ){
            filteredResult = this.getOrdersByClientId( client );

        }else if( status != null ){
            filteredResult = this.getOrdersByOrderStatus( status );

        }else{
            filteredResult = service.getAllOrders()
                    .stream()
                    .map( mapper::map )
                    .sorted( new OrderDateComparator() )
                    .collect( Collectors.toList() );
        }

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( filteredResult )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /order/{id} Get a specific order
     * @apiDescription Fetches an existing order by its <code>id</code> and returns its details.
     * @apiUse getOrderDTO
     * @apiGroup Order
     * @apiName getById
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getById( @PathVariable( name = "id" ) Integer id ){

        Order order = service.getOrder( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.map( order ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region POST

    /**
     * @api {post} /order Create a new order
     * @apiDescription Creates a new order of the submitted values and returns its details.
     * @apiUse createOrderDTO
     * @apiUse getOrderDTO
     * @apiGroup Order
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
    public ResponseEntity<Response> post( @RequestBody OrderCreateDTO dto ){

        Order order = service.createOrder( SecurityContext.getUser().getId(), dto.getClientId(), dto.getComments() );

        Response response = new Response.Builder()
                .meta( Meta.created() )
                .data( mapper.map( order ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    //endregion

    //region PUT

    /**
     * @api {put} /order/{id} Update a specific order
     * @apiDescription Updates an existing order with the submitted values.
     * @apiUse updateOrderDTO
     * @apiGroup Order
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
    public ResponseEntity<Response> update( @PathVariable( name = "id" ) Integer id, @Valid @RequestBody OrderUpdateDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.updateOrder( id, dto.getClientId(), dto.getComments(), dto.getStatus() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {put} /order/{id}/items Update an order's items
     * @apiDescription Looks up products and menus with the <code>id</code>s specified in the request body, and adds
     * them to the group if they exist.
     * @apiUse updateOrderItemsDTO
     * @apiGroup Order
     * @apiName updateItems
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {mime} Content-Type The media type of which the request body must be
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}/items",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> updateItems( @PathVariable( name = "id" ) Integer id, @Valid @RequestBody OrderUpdateItemsDTO dto, BindingResult result ){

        if( result.hasErrors() )
            return ResourceUtils.createErrorResponse( result );

        service.addOrderablesToOrder( id, dto.getItems() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region DELETE

    /**
     * @api {delete} /order/{id} Delete a specific order
     * @apiDescription Deletes an existing order matching <code>id</code>.
     * @apiGroup Order
     * @apiName delete
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}",
            method = RequestMethod.DELETE )
    public ResponseEntity<Response> delete( @PathVariable( name = "id" ) Integer id ){

        service.removeOrder( id );

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {delete} /order/{id}/items Remove an order's items
     * @apiDescription Looks up products and menus with the <code>id</code>s specified in the request body, and removes
     * them from the group if they exist.
     * @apiUSe removeOrderItemsDTO
     * @apiGroup Order
     * @apiName deleteItems
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}/items",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> deleteItems( @PathVariable( name = "id" ) Integer id, @RequestBody OrderRemoveItemsDTO dto ){

        if( dto.getItems() == null )
            service.removeAllOrderablesFromOrder( id );
        else
            service.removeOrderablesFromOrder( id, dto.getItems() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    // endregion

    private Set<OrderDTO> getOrdersByClientId( String clientParam ){

        Set<OrderDTO> set;
        try{
            Integer id = Integer.parseInt( clientParam );

            set = service.getOrdersByClientId( id )
                    .stream()
                    .map( mapper::map )
                    .sorted( new OrderComparator() )
                    .collect( Collectors.toSet() );

        }catch( NumberFormatException e ){
            throw new IllegalParameterException( clientParam, "client" );
        }
        return set;
    }

    private Set<OrderDTO> getOrdersByOrderStatus( String statusParam ){

        Set<OrderDTO> set;
        try{
            OrderStatus status = OrderStatus.valueOf( statusParam.toUpperCase() );

            set = service.getOrdersWithStatus( status )
                    .stream()
                    .map( mapper::map )
                    .collect( Collectors.toSet() );

        }catch( IllegalArgumentException e ){
            throw new IllegalParameterException( statusParam, "status" );
        }
        return set;
    }
}