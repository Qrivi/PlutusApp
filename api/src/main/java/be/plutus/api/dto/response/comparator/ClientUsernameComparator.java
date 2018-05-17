package be.plutus.api.dto.response.comparator;

import be.plutus.api.dto.response.ClientDTO;

import java.util.Comparator;

public class ClientUsernameComparator implements Comparator<ClientDTO>{

    @Override
    public int compare( ClientDTO o1, ClientDTO o2 ){
        return o1.getUsername().compareTo( o2.getUsername() );
    }
}
