package be.plutus.core.model;

public enum TransactionType{
    TOPUP( "top-up" ),
    PAYMENT( "payment" ),
    BREAKEVEN( "break-even" );

    private String type;

    TransactionType( String type ){
        this.type = type;
    }

    @Override
    public String toString(){
        return this.type;
    }
}