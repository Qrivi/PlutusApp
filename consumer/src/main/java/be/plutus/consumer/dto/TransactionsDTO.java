package be.plutus.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class TransactionsDTO{

    @JsonProperty( "offset" )
    private String offset;

    @JsonProperty( "limit" )
    private String limit;

    @JsonProperty( "transactions" )
    private List<TransactionDTO> transactions;

    public TransactionsDTO(){
    }

    public String getOffset(){
        return offset;
    }

    public String getLimit(){
        return limit;
    }

    public List<TransactionDTO> getTransactions(){
        return transactions;
    }
}