package be.plutus.api.dto.response.comparator;

import be.plutus.api.dto.response.OrderDTO;

import java.util.Comparator;

public class OrderDateComparator implements Comparator<OrderDTO>{

    @Override
    public int compare( OrderDTO o1, OrderDTO o2 ){
        return o1.getDate().compareTo( o2.getDate() );
    }
}
