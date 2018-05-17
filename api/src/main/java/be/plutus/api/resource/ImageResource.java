package be.plutus.api.resource;

import be.plutus.api.config.Config;
import be.plutus.api.dto.request.ImageUpdateDTO;
import be.plutus.api.dto.response.comparator.ImageNameComparator;
import be.plutus.api.exception.StorageException;
import be.plutus.api.response.Meta;
import be.plutus.api.response.Response;
import be.plutus.api.util.mapper.ImageMapper;
import be.plutus.api.util.service.FileSystemStorageService;
import be.plutus.api.util.service.MessageService;
import be.plutus.core.model.Image;
import be.plutus.core.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        path = "/image",
        produces = MediaType.APPLICATION_JSON_VALUE )
public class ImageResource{

    private final MessageService messageService;

    private final FileSystemStorageService storage;

    private final ImageService service;
    private final ImageMapper mapper;

    @Autowired
    public ImageResource( MessageService messageService, FileSystemStorageService storage, ImageService service, ImageMapper mapper ){
        this.messageService = messageService;
        this.storage = storage;
        this.service = service;
        this.mapper = mapper;
    }

    // region GET

    /**
     * @api {get} /image Get existing images
     * @apiDescription Fetches existing images and returns their details. If a strict filter is used (e.g
     * <code>name</code>), <code>data</code> will consist of a JSON object rather than a JSON array.
     * @apiUse getImageDTOs
     * @apiGroup Image
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
            filteredResult = mapper.map( service.getImageByName( name ) );

        }else if( like != null ){
            filteredResult = service.getAllImagesByTitleLike( like )
                    .stream()
                    .map( mapper::map )
                    .sorted( new ImageNameComparator() )
                    .collect( Collectors.toList() );

        }else{
            filteredResult = service.getAllImages()
                    .stream()
                    .map( mapper::map )
                    .sorted( new ImageNameComparator() )
                    .collect( Collectors.toList() );
        }

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( filteredResult )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {get} /image/{id} Get a specific image
     * @apiDescription Fetches an existing group by its <code>id</code> and returns its details.
     * @apiUse getImageDTO
     * @apiGroup Image
     * @apiName getById
     * @apiVersion 1.0.0
     * @apiPermission none
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @RequestMapping( value = "/{id}",
            method = RequestMethod.GET )
    public ResponseEntity<Response> getById( @PathVariable( name = "id" ) Integer id ){

        Image image = service.getImage( id );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .data( mapper.map( image ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    // endregion

    // region POST

    /**
     * @api {post} /image Create a new image
     * @apiDescription Creates a new image of the submitted values and returns its details.
     * @apiParam {multipart} file The file to upload
     * @apiUse getImageDTO
     * @apiGroup Image
     * @apiName post
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping(
            method = RequestMethod.POST )
    public ResponseEntity<Response> post( @RequestParam( "file" ) MultipartFile file ){
        //check file size
        if( file.getSize() < 1 || file.getSize() > Config.MAX_FILE_SIZE ){
            Response response = new Response.Builder()
                    .meta( Meta.payloadTooLarge() )
                    .errors( messageService.get( "TooLarge.ImageResource.post" ) )
                    .build();
            return new ResponseEntity<>( response, HttpStatus.PAYLOAD_TOO_LARGE );
        }
        // check media type
        if( !Arrays.asList( Config.ACCEPTED_MIME_TYPES ).contains( file.getContentType() ) ){
            Response response = new Response.Builder()
                    .meta( Meta.unsupportedMediaType() )
                    .errors( messageService.get( "Unsupported.ImageResource.post" ) )
                    .build();
            return new ResponseEntity<>( response, HttpStatus.UNSUPPORTED_MEDIA_TYPE );
        }

        String[] filename = file.getOriginalFilename().split( "\\.(?=[^\\.]+$)" );

        String newname = UUID.randomUUID().toString() + "." + filename[1];
        String oldname = filename[0].substring( 0, Math.min( filename[0].length(), 25 ) ); //max 25 first chars
        Image image = service.createImage( oldname, null, newname );

        try{
            storage.store( file, newname );
        }catch( StorageException storageException ){
            service.removeImage( image.getId() );
            throw storageException;
        }

        Response response = new Response.Builder()
                .meta( Meta.created() )
                .data( mapper.map( image ) )
                .build();

        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    // endregion

    //region PUT

    /**
     * @api {put} /image/{id} Update a specific image
     * @apiDescription Updates an existing image with the submitted values.
     * @apiUse updateImageDTO
     * @apiGroup Image
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
    public ResponseEntity<Response> update( @PathVariable( name = "id" ) Integer id, @RequestBody ImageUpdateDTO dto ){

        service.updateImage( id, dto.getTitle(), dto.getDescription() );

        Response response = new Response.Builder()
                .meta( Meta.success() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    //endregion

    //region DELETE

    /**
     * @api {delete} /image/{id} Delete a specific image
     * @apiDescription Deletes an existing image matching <code>id</code>.
     * @apiGroup Image
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

        Image image = service.getImage( id );

        service.removeImage( id );
        storage.delete( image.getName() );

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * @api {delete} /image Delete all images
     * @apiDescription Deletes all images.
     * @apiGroup Image
     * @apiName deleteAll
     * @apiVersion 1.0.0
     * @apiPermission BOSS
     * @apiHeader {token} X-Auth-Token The current user's identification token
     * @apiHeader {locale} Accept-Language The response language (overridden by the user's preference)
     */
    @Secured( "ROLE_BOSS" )
    @RequestMapping( method = RequestMethod.DELETE )
    public ResponseEntity<Response> deleteAll(){

        service.removeAll();
        storage.deleteAll();

        Response response = new Response.Builder()
                .meta( Meta.noContent() )
                .build();

        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    // endregion
}
