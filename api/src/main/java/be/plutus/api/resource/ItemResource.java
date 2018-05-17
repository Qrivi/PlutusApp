package be.plutus.api.resource;


import be.plutus.api.dto.response.comparator.ItemNameComparator;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.util.mapper.ItemMapper;
import be.plutus.core.model.Item;
import be.plutus.core.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        produces = MediaType.APPLICATION_JSON_VALUE )
public class ItemResource{

    private final ItemService service;
    private final ItemMapper mapper;

    @Autowired
    public ItemResource( ItemService service, ItemMapper mapper ){
        this.service = service;
        this.mapper = mapper;
    }

    //region GET

    /**
     * @api {get} /sellables Get all sellable items
     * @apiDescription Fetches all products, groups and menu templates that are set to be directly sellable
     * (<code>selectable = true</code>).
     * @apiUse getItemDTOs
     * @apiGroup Item
     * @apiName getAll
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/sellables",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getAll(){

        List<Item> items = service.getAllItems();

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( items.stream().
                        map( mapper::map )
                        .filter( Objects::nonNull )
                        .sorted( new ItemNameComparator() )
                        .collect( Collectors.toList() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /identify/{id} Identify a specific item
     * @apiDescription Fetches an existing item by its <code>id</code> and returns its details.
     * @apiUse getIdentifyDTO
     * @apiGroup Item
     * @apiName getById
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/identify/{id}",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getById( @PathVariable( name = "id" ) Integer id ){

        Item item = service.getItem( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.identify( item ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }
}