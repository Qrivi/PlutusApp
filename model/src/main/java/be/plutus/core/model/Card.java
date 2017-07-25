package be.plutus.core.model;

import be.plutus.common.CryptoService;
import be.plutus.common.Identifiable;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table( name = "card" )
public class Card extends Identifiable{

    @NotBlank( message = "{NotBlank.Card.customerId}" )
    @Size( max = 8, message = "{Size.Card.customerId}" )
    @Column( name = "customer_id", unique = true )
    private String customerId;

    @Size( max = 64, message = "{Size.Card.apiKey}" )
    @Column( name = "api_key" )
    private String apiKey;

    @NotBlank( message = "{NotBlank.Card.number}" )
    @Size( max = 8, message = "{Size.Card.number}" )
    @Column( name = "number", unique = true )
    private String number;

    @NotNull( message = "{NotNull.Card.status}" )
    @Column( name = "status" )
    @Enumerated( EnumType.STRING )
    private CardStatus status;

    @NotBlank( message = "{NotBlank.Card.name}" )
    @Size( max = 45, message = "{Size.Card.name}" )
    @Column( name = "name" )
    private String name;

    @NotBlank( message = "{NotBlank.Card.alias}" )
    @Size( max = 20, message = "{Size.Card.alias}" )
    @Column( name = "alias" )
    private String alias;

    @NotNull( message = "{NotNull.Card.creationDate}" )
    @Column( name = "creation" )
    private ZonedDateTime creationDate;

    @Size( max = 45, message = "{Size.Card.email}" )
    @Column( name = "email", unique = true )
    private String email;

    @NotBlank( message = "{NotBlank.Card.uuid}" )
    @Size( max = 36, message = "{Size.Card.uuid}" )
    @Column( name = "uuid" )
    private String uuid;

    @NotBlank( message = "{NotBlank.Card.password}" )
    @Size( max = 255, message = "{Size.Card.password}" ) // TODO set better length
    @Column( name = "password" )
    private String password;

    @NotNull( message = "{NotNull.Card.language}" )
    @Enumerated( EnumType.STRING )
    @Column( name = "language" )
    private CardLanguage language;

    @Column( name = "credit" )
    private Double credit;

    @Column( name = "weekspent" )
    private Double weekSpent;

    @Column( name = "alert_lowcredit" )
    private Integer alertLowCredit;

    public Card(){
    }

    public String getCustomerId(){
        return customerId;
    }

    public void setCustomerId( String customerId ){
        this.customerId = customerId;
    }

    public String getApiKey(){
        return apiKey;
    }

    public void setApiKey( String apiKey ){
        this.apiKey = apiKey;
    }

    public String getNumber(){
        return number;
    }

    public void setNumber( String number ){
        this.number = number.toLowerCase();
    }

    public CardStatus getStatus(){
        return status;
    }

    public void setStatus( CardStatus status ){
        this.status = status;
    }

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    public String getAlias(){
        return alias;
    }

    public void setAlias( String alias ){
        this.alias = alias;
    }

    public ZonedDateTime getCreationDate(){
        return creationDate;
    }

    public void setCreationDate( ZonedDateTime creationDate ){
        this.creationDate = creationDate;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail( String email ){
        this.email = email;
    }

    public String getUuid(){
        return uuid;
    }

    public void generateUuid(){
        this.uuid = UUID.randomUUID().toString();
    }

    public String getPassword(){
        return CryptoService.decrypt( password, uuid );
    }

    public void setPassword( String plainTextPassword ){
        if( "".equals( uuid ) )
            this.generateUuid();
        this.password = CryptoService.encrypt( plainTextPassword, this.uuid );
    }

    public boolean isPasswordValid( String plainTextPassword ){
        if( plainTextPassword == null || this.password == null )
            return false;
        return Objects.equals( this.password, CryptoService.encrypt( plainTextPassword, this.uuid ) );

    }

    public CardLanguage getLanguage(){
        return language;
    }

    public void setLanguage( CardLanguage language ){
        this.language = language;
    }

    public Double getCredit(){
        return credit;
    }

    public void setCredit( Double credit ){
        this.credit = credit;
    }

    public Double getWeekSpent(){
        return weekSpent;
    }

    public void setWeekSpent( Double weekSpent ){
        this.weekSpent = weekSpent;
    }

    public Integer getAlertLowCredit(){
        return alertLowCredit;
    }

    public void setAlertLowCredit( Integer alertLowCredit ){
        this.alertLowCredit = alertLowCredit;
    }
}
