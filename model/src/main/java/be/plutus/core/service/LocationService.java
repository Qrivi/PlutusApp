package be.plutus.core.service;

import be.plutus.core.model.Campus;
import be.plutus.core.model.Location;

import java.util.List;

public interface LocationService{

    List<Location> getAllLocations();

    List<Location> getLocationsByCity( Campus campus );

    Location getLocationById( Integer id );

    Location getLocationByLabel( String label );

    Location getLocationByName( String name );

    Location createLocation( String label, String name, Campus campus );

    void updateLocation( int id, String name, Campus campus );

    void removeLocation( int id );
}
