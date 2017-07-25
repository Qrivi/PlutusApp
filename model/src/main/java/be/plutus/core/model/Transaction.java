package be.plutus.core.model;

import be.plutus.common.Identifiable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<Product> products;

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

    public List<Product> getProducts(){
        return products;
    }

    public void setProducts( List<Product> products ){
        this.products = products;
    }

    public void addProduct( Product product ){
        if( products == null )
            products = new ArrayList<>();
        products.add( product );
    }

    public void removeProduct( Product product ){
        if( products == null )
            products = new ArrayList<>();
        else
            products.remove( product );
    }

    public void removeAllProducts(){
        if( products == null )
            products = new ArrayList<>();
        else
            products.clear();
    }

    public double getAmount(){
        return Math.abs( this.getProductPriceTotal() );
    }

    public TransactionType getType(){
        double amount = this.getProductPriceTotal();
        if( amount < 0 )
            return TransactionType.PAYMENT;
        else if( amount > 0 )
            return TransactionType.TOPUP;
        else
            return TransactionType.BREAKEVEN;
    }

    private double getProductPriceTotal(){
        return products.stream()
                .mapToDouble( p -> p.getType() == ProductType.CREDIT ? p.getPrice() : p.getPrice() * -1 )
                .sum();
    }
}
