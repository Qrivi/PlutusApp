package be.plutus.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan( {"be.plutus"} )
@EnableJpaRepositories( basePackages = {"be.plutus"} )
@SpringBootApplication( scanBasePackages = {"be.plutus"} )
public class Application{

    public static void main( String[] args ){
        SpringApplication.run( Application.class, args );
    }
}