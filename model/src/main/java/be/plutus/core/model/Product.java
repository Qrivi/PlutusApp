package be.plutus.core.model;

import be.plutus.common.Identifiable;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table( name = "product" )
public class Product extends Identifiable{

    @NotBlank( message = "{NotBlank.Product.label}" )
    @Column( name = "label", unique = true )
    private String label;

    @Column( name = "name", unique = true )
    private String name;

    @Min( value = 0, message = "{Min.Product.price}" )
    @Column( name = "price")
    private double price;

    public Product(){
    }

    public String getLabel(){
        return label;
    }

    public void setLabel( String label ){
        this.label = label;
    }

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice( double price ){
        this.price = price;
    }
}
