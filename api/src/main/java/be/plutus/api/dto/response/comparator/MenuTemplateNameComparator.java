package be.plutus.api.dto.response.comparator;

import be.plutus.api.dto.response.MenuTemplateDTO;

import java.util.Comparator;

public class MenuTemplateNameComparator implements Comparator<MenuTemplateDTO>{

    @Override
    public int compare( MenuTemplateDTO o1, MenuTemplateDTO o2 ){
        return o1.getName().compareTo( o2.getName() );
    }
}
