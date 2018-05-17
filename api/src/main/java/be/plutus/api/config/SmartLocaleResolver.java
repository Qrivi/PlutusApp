package be.plutus.api.config;

import be.plutus.api.security.context.SecurityContext;
import be.plutus.core.model.user.User;
import be.plutus.core.model.user.UserLanguage;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class SmartLocaleResolver extends AcceptHeaderLocaleResolver{

    @Override
    public Locale resolveLocale( HttpServletRequest request ){

        User user = SecurityContext.getUser();
        if( user != null ){
            UserLanguage userLanguage = user.getLanguage();
            if( userLanguage != null )
                return userLanguage.getLocale();
        }
        String acceptLanguage = request.getHeader( "Accept-Language" );
        if( acceptLanguage == null || acceptLanguage.equals( "" ) )
            return getDefaultLocale();

        return super.resolveLocale( request );
    }
}