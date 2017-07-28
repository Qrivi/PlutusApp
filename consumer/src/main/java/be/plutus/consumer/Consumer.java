package be.plutus.consumer;

import be.plutus.common.DateService;
import be.plutus.consumer.dto.ApiKeyDTO;
import be.plutus.core.model.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Consumer{

    private final RestTemplate rest;

    @Autowired
    public Consumer( RestTemplate rest ){
        this.rest = rest;
    }

    public void requestApiKey( Credentials credentials ){

        Resource res = new Resource.Builder()
                .username( credentials.getCardNumber() )
                .password( credentials.getPassword() )
                .locationcode( "UCLL" )
                .deviceid( credentials.getDeviceId() )
                .devicedescription( credentials.getDeviceName() )
                .UTC( DateService.unix() )
                .build();

        res.sign( credentials.getKey() );
        
        ApiKeyDTO dto = rest.getForObject( res.toURL(), ApiKeyDTO.class );

        // todo
        // Data uit dto verwerken en methode opsplitsen in case credentials niet gegeven is aka de gebruiker gebruikt
        // de app voor de allereerste keer ooit --> er zitten geen credentials in de database.
    }
}



