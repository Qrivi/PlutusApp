package be.plutus.core.model;

public enum ProductType{
    DEBET( "debet" ),
    CREDIT( "credit" ),
    ZERO( "zero" );

    private String type;

    ProductType( String type ){
        this.type = type;
    }

    @Override
    public String toString(){
        return this.type;
    }
}
