package be.plutus.api.dto.response.comparator;

import be.plutus.api.dto.response.MenuDTO;

import java.util.Comparator;

public class MenuNameComparator implements Comparator<MenuDTO>{

    @Override
    public int compare( MenuDTO o1, MenuDTO o2 ){
        return o1.getName().compareTo( o2.getName() );
    }
}
