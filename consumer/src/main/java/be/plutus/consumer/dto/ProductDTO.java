package be.plutus.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ProductDTO{

    @JsonProperty( "itemName" )
    private String name;

    @JsonProperty( "quantity" )
    private Integer quantity;

    @JsonProperty( "amount" )
    private Double price;

    @JsonProperty( "debitCredit" )
    private String productType;

    @JsonProperty( "transactionType" )
    private Integer transactionType;

    public ProductDTO(){
    }

    public String getName(){
        return name;
    }

    public Integer getQuantity(){
        return quantity;
    }

    public Double getPrice(){
        return price;
    }

    public String getProductType(){
        return productType;
    }

    public Integer getTransactionType(){
        return transactionType;
    }
}