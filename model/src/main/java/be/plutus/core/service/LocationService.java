package be.plutus.core.service;

import be.plutus.core.model.Campus;
import be.plutus.core.model.Label;
import be.plutus.core.model.Location;

import java.util.List;

public interface LocationService{

    List<Location> getAllLocations();

    List<Location> getLocationsByCity( Campus campus );

    Location getLocationById( Integer id );

    Location getLocationByLabel( Label label );

    Location createLocation( Label label, Campus campus );

    void updateLocation( int id, Campus campus );

    void removeLocation( int id );
}
