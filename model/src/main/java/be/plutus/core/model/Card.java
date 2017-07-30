package be.plutus.core.model;

import be.plutus.common.Identifiable;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table( name = "card" )
public class Card extends Identifiable{

    @Valid
    @NotNull( message = "{NotNull.Card.credentials}" )
    @OneToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = "credentials_id" )
    private Credentials credentials;

    @NotBlank( message = "{NotBlank.Card.name}" )
    @Size( max = 45, message = "{Size.Card.name}" )
    @Column( name = "name" )
    private String name;

    @Size( max = 20, message = "{Size.Card.alias}" )
    @Column( name = "alias" )
    private String alias;

    @NotNull( message = "{NotNull.Card.language}" )
    @Enumerated( EnumType.STRING )
    @Column( name = "language" )
    private CardLanguage language;

    @NotNull( message = "{NotNull.Card.status}" )
    @Column( name = "card_status" )
    @Enumerated( EnumType.STRING )
    private CardStatus status;

    @NotNull( message = "{NotNull.Card.emailStatus}" )
    @Column( name = "email_status" )
    @Enumerated( EnumType.STRING )
    private CardEmailStatus emailStatus;

    @Email( message = "{Email.Card.email}" )
    @Size( max = 45, message = "{Size.Card.email}" )
    @Column( name = "email", unique = true )
    private String email;

    @NotNull( message = "{NotNull.Card.creationDate}" )
    @Column( name = "creation" )
    private ZonedDateTime creationDate;

    @Column( name = "credit" )
    private Double credit;

    @Column( name = "weekspent" )
    private Double weekSpent;

    @Column( name = "alert_lowcredit" )
    private Integer alertLowCredit;

    public Card(){
    }

    public Credentials getCredentials(){
        return credentials;
    }

    public void setCredentials( Credentials credentials ){
        this.credentials = credentials;
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

    public CardLanguage getLanguage(){
        return language;
    }

    public void setLanguage( CardLanguage language ){
        this.language = language;
    }

    public CardStatus getStatus(){
        return status;
    }

    public void setStatus( CardStatus status ){
        this.status = status;
    }

    public CardEmailStatus getEmailStatus(){
        return emailStatus;
    }

    public void setEmailStatus( CardEmailStatus emailStatus ){
        this.emailStatus = emailStatus;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail( String email ){
        this.email = email;
    }

    public ZonedDateTime getCreationDate(){
        return creationDate;
    }

    public void setCreationDate( ZonedDateTime creationDate ){
        this.creationDate = creationDate;
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
