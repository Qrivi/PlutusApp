package be.plutus.api.dto.response.comparator;

import be.plutus.api.dto.response.ImageDTO;

import java.util.Comparator;

public class ImageNameComparator implements Comparator<ImageDTO>{

    @Override
    public int compare( ImageDTO o1, ImageDTO o2 ){
        return o1.getName().compareTo( o2.getName() );
    }
}
