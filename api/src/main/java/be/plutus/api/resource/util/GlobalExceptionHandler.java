package be.plutus.api.resource.util;

import be.plutus.api.exception.IllegalParameterException;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.util.service.MessageService;
import be.plutus.core.exception.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler{

    private final MessageService messageService;

    @Autowired
    public GlobalExceptionHandler( MessageService messageService ){
        this.messageService = messageService;
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleTransactionSystemException( TransactionSystemException e ){

        // altijd gethrowed als hibernate resultaat niet kan mappen zonder constraints te breken
        ConstraintViolationException ex = (ConstraintViolationException)e.getRootCause();

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( ex.getConstraintViolations()
                        .stream()
                        .map( ConstraintViolation::getMessageTemplate )
                        .map( x -> x.replaceAll( "[\\{\\}]", "" ) )
                        .map( messageService::get )
                        .collect( Collectors.toList() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleInvalidDataAccessApiUsageException( InvalidDataAccessApiUsageException e ){

        Response response = new Response.Builder()
                .meta( Meta.internalServerError() )
                .errors( messageService.get( "GlobalExceptionHandler.databaseError" ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleCannotCreateTransactionException( CannotCreateTransactionException e ){

        Response response = new Response.Builder()
                .meta( Meta.internalServerError() )
                .errors( messageService.get( "GlobalExceptionHandler.databaseNotFound" ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleHttpRequestMethodNotSupportedException( MissingServletRequestPartException e ){

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( messageService.get( "GlobalExceptionHandler.missingRequestPart" ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleHttpMessageNotReadableException( HttpMessageNotReadableException e ){

        JsonMappingException ex = (JsonMappingException)e.getRootCause();

        String key = ex.getPathReference().substring(
                ex.getPathReference().indexOf( "\"" ) + 1,
                ex.getPathReference().lastIndexOf( "\"" ) );

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( messageService.get( "GlobalExceptionHandler.MessageNotReadable", key ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleHttpMediaTypeNotSupportedException( HttpMediaTypeNotSupportedException e ){

        Response response = new Response.Builder()
                .meta( Meta.unsupportedMediaType() )
                .errors( messageService.get( "GlobalExceptionHandler.ContentTypeNotSupported" ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.UNSUPPORTED_MEDIA_TYPE );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleHttpRequestMethodNotSupportedException( HttpRequestMethodNotSupportedException e ){

        Response response = new Response.Builder()
                .meta( Meta.methodNotAllowed() )
                .errors( messageService.get( "GlobalExceptionHandler.methodNotAllowed", e.getMethod() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.METHOD_NOT_ALLOWED );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleNoHandlerFoundException( NoHandlerFoundException e ){

        Response response = new Response.Builder()
                .meta( Meta.notFound() )
                .errors( messageService.get( "GlobalExceptionHandler.notAnEndpoint", e.getHttpMethod(), e.getRequestURL() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleMethodArgumentTypeMismatchException( MethodArgumentTypeMismatchException e ){

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( messageService.get( "GlobalExceptionHandler.argumentTypeMismatch" ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleInvalidIdentifierException( InvalidIdentifierException e ){

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( messageService.get( e.getClass().getName() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleDuplicateException( DuplicateException e ){

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( messageService.get( e.getClass().getName(), e.getKey() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleOrderNotNullException( OrderNotNullException e ){

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( messageService.get( e.getClass().getName() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleIllegalParameterException( IllegalParameterException e ){

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( messageService.get( e.getClass().getName(), e.getParameterValue(), e.getParameterName() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleIllegalMenuCombinationException( IllegalMenuCombinationException e ){

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( messageService.get( e.getClass().getName(), e.getMenu().getTemplate().getName() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleIllegalMenuCombinationException( IllegalOrderActionException e ){

        Response response = new Response.Builder()
                .meta( Meta.badRequest() )
                .errors( messageService.get( e.getClass().getName() ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler
    public ResponseEntity<Response> handleNullPointerException( NullPointerException e ){

        return ResourceUtils.createNotFoundResponse();
    }
}
