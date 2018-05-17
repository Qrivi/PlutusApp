package be.plutus.api.security.context;

import be.plutus.core.model.Card;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContext{

    public static Card getCard(){
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if( auth == null )
            return null;

        return (Card)auth.getPrincipal();
    }
}
