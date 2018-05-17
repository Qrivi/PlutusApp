package be.plutus.api.dto.response.comparator;

import be.plutus.api.dto.response.ProductDTO;

import java.util.Comparator;

public class ProductNameComparator implements Comparator<ProductDTO>{

    @Override
    public int compare( ProductDTO o1, ProductDTO o2 ){
        return o1.getName().compareTo( o2.getName() );
    }
}
