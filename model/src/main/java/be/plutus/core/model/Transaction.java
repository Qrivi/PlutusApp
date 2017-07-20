package be.plutus.core.model;

import be.plutus.common.Identifiable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table( name = "transaction" )
public class Transaction extends Identifiable{

    @NotNull( message = "{NotNull.Transaction.timestamp}" )
    @Column( name = "timestamp" )
    private ZonedDateTime timestamp;

    @Valid
    @NotNull( message = "{NotNull.Transaction.card}" )
    @ManyToOne
    @JoinColumn( name = "card_id" )
    private Card card;

    @Valid
    @NotNull( message = "{NotNull.Transaction.location}" )
    @ManyToOne
    @JoinColumn( name = "location_id" )
    private Location location;

    @NotNull( message = "{NotNull.Transaction.products}" )
    @ManyToMany
    @JoinTable(
            name = "transaction_products",
            joinColumns = @JoinColumn( name = "transaction_id", referencedColumnName = "id" ),
            inverseJoinColumns = @JoinColumn( name = "product_id", referencedColumnName = "id" ) )
    private Set<Product> products;

    public Transaction(){
    }

    public ZonedDateTime getTimestamp(){
        return timestamp;
    }

    public void setTimestamp( ZonedDateTime timestamp ){
        this.timestamp = timestamp;
    }

    public Card getCard(){
        return card;
    }

    public void setCard( Card card ){
        this.card = card;
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation( Location location ){
        this.location = location;
    }

    public double getAmount(){
        return products.stream()
                .mapToDouble( p -> getAmount() )
                .sum();
    }

    public TransactionType getType(){
        double amount = this.getAmount();
        if( amount < 0 )
            return TransactionType.PAYMENT;
        else if( amount > 0 )
            return TransactionType.TOPUP;
        else
            return TransactionType.BREAKEVEN;
    }
}
