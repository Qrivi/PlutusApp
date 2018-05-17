package be.plutus.api.resource.util;

import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ResourceUtils{

    public static ResponseEntity<Response> createErrorResponse( BindingResult result ){
        Response.Builder response = new Response.Builder();
        response.errors( result.getAllErrors()
                .stream()
                .map( DefaultMessageSourceResolvable::getDefaultMessage )
                .collect( Collectors.toList() )
        );
        return new ResponseEntity<>( response.meta( Meta.badRequest() ).build(), HttpStatus.BAD_REQUEST );
    }

    public static ResponseEntity<Response> createNotFoundResponse(){
        Response response = new Response.Builder()
                .meta( Meta.notFound() )
                .build();
        return new ResponseEntity<>( response, HttpStatus.NOT_FOUND );
    }
}