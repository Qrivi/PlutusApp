package be.plutus.consumer;

import be.plutus.common.DateService;
import be.plutus.consumer.dto.ApiKeyDTO;
import be.plutus.core.model.Credentials;
import be.plutus.core.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Consumer{

    private final RestTemplate rest;
    private final CredentialsService credentialsService;

    @Autowired
    public Consumer( RestTemplate rest, CredentialsService credentialsService ){
        this.rest = rest;
        this.credentialsService = credentialsService;
    }

    private ApiKeyDTO handleRequest( Credentials credentials ){
        HttpHeaders headers = new HttpHeaders();
        Resource resource = new Resource.Builder()
                .username( credentials.getCardNumber() )
                .password( credentials.getPassword() )
                .locationcode( "UCLL" )
                .deviceid( credentials.getDeviceId() )
                .devicedescription( credentials.getDeviceName() )
                .UTC( DateService.unix() )
                .build();

        headers.set( HttpHeaders.USER_AGENT, credentials.getUserAgent() );
        resource.sign( credentials.getKey() );


        ResponseEntity<ApiKeyDTO> response = rest.exchange(
                resource.toURL(),
                HttpMethod.GET,
                new HttpEntity( headers ),
                ApiKeyDTO.class
        );

        if( response.getStatusCode() == HttpStatus.OK )
            return response.getBody();
        return null;
    }
}



