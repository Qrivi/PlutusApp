package be.plutus.common;

import be.plutus.core.model.CredentialsIdentity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class IdentifierService{

    public static String generateUUID(){
        return UUID.randomUUID().toString();
    }

    public static String generateAndroidID(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while( sb.length() < 16 )
            sb.append( Integer.toHexString( random.nextInt() ) );
        return sb.toString();
    }

    public static CredentialsIdentity generateIdentity(){
        List<CredentialsIdentity> identities = Arrays.asList( CredentialsIdentity.values() );
        return identities.get( ( new Random() ).nextInt( identities.size() ) );
    }
}