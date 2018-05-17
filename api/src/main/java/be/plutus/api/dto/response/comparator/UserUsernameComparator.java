package be.plutus.api.dto.response.comparator;

import be.plutus.api.dto.response.UserDTO;

import java.util.Comparator;

public class UserUsernameComparator implements Comparator<UserDTO>{

    @Override
    public int compare( UserDTO o1, UserDTO o2 ){
        return o1.getUsername().compareTo( o2.getUsername() );
    }
}
