package be.plutus.api.dto.response.comparator;

import be.plutus.api.dto.response.GroupDTO;

import java.util.Comparator;

public class GroupNameComparator implements Comparator<GroupDTO>{

    @Override
    public int compare( GroupDTO o1, GroupDTO o2 ){
        return o1.getName().compareTo( o2.getName() );
    }
}
