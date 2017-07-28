package be.plutus.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class TransactionDTO{

    @JsonProperty( "lines" )
    List<ProductDTO> products;
    @JsonProperty( "dateTimeUTC" )
    private ZonedDateTime timestamp;
    @JsonProperty( "stationType" )
    private Integer stationType;
    @JsonProperty( "stationName" )
    private String stationName;
    @JsonProperty( "storeName" )
    private String storeName;

    public TransactionDTO(){
    }

    public ZonedDateTime getTimestamp(){
        return timestamp;
    }

    public Integer getStationType(){
        return stationType;
    }

    public String getStationName(){
        return stationName;
    }

    public String getStoreName(){
        return storeName;
    }

    public List<ProductDTO> getProducts(){
        return products;
    }
}