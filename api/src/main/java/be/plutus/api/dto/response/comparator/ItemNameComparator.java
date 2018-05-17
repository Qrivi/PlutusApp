package be.plutus.api.dto.response.comparator;

import be.plutus.api.dto.response.ItemDTO;

import java.util.Comparator;

public class ItemNameComparator implements Comparator<ItemDTO>{

    @Override
    public int compare( ItemDTO o1, ItemDTO o2 ){
        return o1.getName().compareTo( o2.getName() );
    }
}
