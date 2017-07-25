package be.plutus.core.service;

import be.plutus.core.exception.DuplicateLocationException;
import be.plutus.core.exception.InvalidLocationIdentifierException;
import be.plutus.core.model.Campus;
import be.plutus.core.model.Location;
import be.plutus.core.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocationJPAService implements LocationService{

    private final LocationRepository locationRepository;

    @Autowired
    public LocationJPAService( LocationRepository locationRepository ){
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }

    @Override
    public List<Location> getLocationsByCity( Campus campus ){
        return locationRepository.findByCampus( campus );
    }

    @Override
    public Location getLocationById( Integer id ){
        if( id == null )
            throw new InvalidLocationIdentifierException();
        return locationRepository.findOne( id );
    }

    @Override
    public Location getLocationByLabel( String label ){
        return locationRepository.findByLabel( label );
    }

    @Override
    public Location getLocationByName( String name ){
        return locationRepository.findByName( name );
    }

    @Override
    public Location createLocation( String label, String name, Campus campus ){
        Location location = new Location();

        if( this.getLocationByLabel( label ) != null )
            throw new DuplicateLocationException( label );

        if( this.getLocationByName( name ) != null )
            throw new DuplicateLocationException( name );

        location.setLabel( label );
        location.setName( name );
        location.setCampus( campus );

        return locationRepository.save( location );
    }

    @Override
    public void updateLocation( int id, String name, Campus campus ){
        Location location = this.getLocationById( id );

        Location unique = this.getLocationByName( name );
        if( unique != null && unique != location )
            throw new DuplicateLocationException( name );

        if( name != null )
            location.setName( name );
        if( campus != null )
            location.setCampus( campus );

        locationRepository.save( location );
    }

    @Override
    public void removeLocation( int id ){
        Location location = this.getLocationById( id );

        locationRepository.delete( location );
    }
}
