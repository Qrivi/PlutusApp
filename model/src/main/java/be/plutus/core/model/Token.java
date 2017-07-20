package be.plutus.core.model;

import be.plutus.common.Identifiable;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table( name = "token" )
public class Token extends Identifiable{

    @Valid
    @NotNull( message = "{NotNull.Token.card}" )
    @ManyToOne
    @JoinColumn( name = "card_id" )
    private Card card;

    @NotBlank( message = "{NotBlank.Token.token}" )
    @Column( name = "token", unique = true )
    private String token;

    @NotBlank( message = "{NotBlank.Token.client}" )
    @Column( name = "client" )
    private String client;

    @NotNull( message = "{NotNull.Token.creationDate}" )
    @Column( name = "creation" )
    private ZonedDateTime creationDate;

    @NotNull( message = "{NotNull.Token.expirationDate}" )
    @Column( name = "expiration" )
    private ZonedDateTime expirationDate;

    @NotNull( message = "{NotNull.Token.status}")
    @Enumerated( EnumType.STRING )
    @Column( name = "status" )
    private TokenStatus status;

    public Token(){
    }

    public Card getCard(){
        return card;
    }

    public void setCard( Card card ){
        this.card = card;
    }

    public String getToken(){
        return token;
    }

    public void setToken( String token ){
        this.token = token;
    }

    public String getClient(){
        return client;
    }

    public void setClient( String client ){
        this.client = client;
    }

    public ZonedDateTime getCreationDate(){
        return creationDate;
    }

    public void setCreationDate( ZonedDateTime creationDate ){
        this.creationDate = creationDate;
    }

    public ZonedDateTime getExpirationDate(){
        return expirationDate;
    }

    public void setExpirationDate( ZonedDateTime expirationDate ){
        this.expirationDate = expirationDate;
    }

    public TokenStatus getStatus(){
        return status;
    }

    public void setStatus( TokenStatus status ){
        this.status = status;
    }
}
