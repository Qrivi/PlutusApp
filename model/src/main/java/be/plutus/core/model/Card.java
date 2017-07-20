package be.plutus.core.model;

import be.plutus.common.Identifiable;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table( name = "card" )
public class Card extends Identifiable{

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
    private LocalDateTime creationDate;

    @Size( max = 45, message = "{Size.Card.email}" )
    @Column( name = "email", unique = true )
    private String email;

    @NotBlank( message = "{NotBlank.Card.hash}" )
    @Size( max = 40, message = "{Size.Card.hash}" )
    @Column( name = "hash" )
    private String hash;

    @NotBlank( message = "{NotBlank.Card.salt}" )
    @Size( max = 40, message = "{Size.Card.salt}" )
    @Column( name = "salt" )
    private String salt;

    @NotBlank( message = "{NotBlank.Card.password}" )
    @Size( max = 255, message = "{Size.Card.password}" )
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

    public String getNumber(){
        return number;
    }

    public void setNumber( String number ){
        this.number = number;
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

    public LocalDateTime getCreationDate(){
        return creationDate;
    }

    public void setCreationDate( LocalDateTime creationDate ){
        this.creationDate = creationDate;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail( String email ){
        this.email = email;
    }

    public String getHash(){
        return hash;
    }

    public void setHash( String hash ){
        this.hash = hash;
    }

    public String getSalt(){
        return salt;
    }

    public void setSalt( String salt ){
        this.salt = salt;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword( String password ){
        this.password = password;
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
