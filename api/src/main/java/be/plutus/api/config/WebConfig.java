package be.plutus.api.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter{

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // messageSource.setUseCodeAsDefaultMessage( true ); // early development only
        messageSource.setBasenames( "classpath:messages" );
        messageSource.setDefaultEncoding( "UTF-8" );
        messageSource.setAlwaysUseMessageFormat( true );
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver(){
        AcceptHeaderLocaleResolver localeResolver = new SmartLocaleResolver();
        localeResolver.setDefaultLocale( Config.DEFAULT_LANGUAGE.getLocale() );
        return localeResolver;
    }

    @Override
    public Validator getValidator(){
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource( messageSource() );
        return validator;
    }
}