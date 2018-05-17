package be.plutus.api.config;

import be.plutus.api.security.filter.TokenAuthenticationFilter;
import be.plutus.api.util.service.MessageService;
import be.plutus.core.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( securedEnabled = true )
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final TokenService tokenService;

    private final ObjectMapper objectMapper;

    private final MessageService messageService;

    @Autowired
    public SecurityConfig( TokenService tokenService, ObjectMapper objectMapper, MessageService messageService ){
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
        this.messageService = messageService;
    }

    @Override
    public void configure( WebSecurity web ) throws Exception{
        web
                .ignoring()
                .antMatchers( HttpMethod.POST, "/auth" )
                .antMatchers( HttpMethod.GET, "/sellables" )
                .antMatchers( HttpMethod.GET, "/product/**" )
                .antMatchers( HttpMethod.GET, "/menutemplate/**" );
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception{
        http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .addFilterBefore( new TokenAuthenticationFilter( tokenService, objectMapper, messageService ), BasicAuthenticationFilter.class )
                .sessionManagement()
                .sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .anonymous().disable();
    } //TODO need help setting this filter to trigger AFTER the locale is resolved
}
