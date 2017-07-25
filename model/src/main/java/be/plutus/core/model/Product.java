package be.plutus.core.model;

import be.plutus.common.Identifiable;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "product" )
public class Product extends Identifiable{

    @NotBlank( message = "{NotBlank.Product.label}" )
    @Column( name = "label", unique = true )
    private String label;

    @Column( name = "name" )
    private String name;

    @NotNull( message = "{NotNull.Product.price}" )
    @Min( value = 0, message = "{Min.Product.price}" )
    @Column( name = "price" )
    private Double price;

    @NotNull( message = "{NotNull.Product.type}" )
    @Enumerated( EnumType.STRING )
    @Column( name = "type" )
    private ProductType type;

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
        if( price == 0 ){
            this.price = 0.0;
            this.type = ProductType.ZERO;
        }else{
            this.price = Math.abs( price );
            this.type = price > 0 ? ProductType.CREDIT : ProductType.DEBET;
        }
    }

    public ProductType getType(){
        return type;
    }
}
