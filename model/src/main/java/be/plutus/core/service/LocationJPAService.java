package be.plutus.core.service;

import be.plutus.core.exception.DuplicateLocationException;
import be.plutus.core.exception.InvalidLocationIdentifierException;
import be.plutus.core.model.Campus;
import be.plutus.core.model.Label;
import be.plutus.core.model.Location;
import be.plutus.core.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocationJPAService implements LocationService{

    private final LocationRepository repository;

    @Autowired
    public LocationJPAService( LocationRepository repository ){
        this.repository = repository;
    }

    @Override
    public List<Location> getAllLocations(){
        return repository.findAll();
    }

    @Override
    public List<Location> getLocationsByCity( Campus campus ){
        return repository.findByCampus( campus );
    }

    @Override
    public Location getLocationById( Integer id ){
        if( id == null )
            throw new InvalidLocationIdentifierException();
        return repository.findOne( id );
    }

    @Override
    public Location getLocationByLabel( Label label ){
        return repository.findByLabel( label );
    }

    @Override
    public Location createLocation( Label label, Campus campus ){
        Location location = new Location();

        if( this.getLocationByLabel( label ) != null )
            throw new DuplicateLocationException( label );

        location.setLabel( label );
        location.setCampus( campus );

        return repository.save( location );
    }

    @Override
    public void updateLocation( int id, Campus campus ){
        Location location = this.getLocationById( id );

        if( campus != null )
            location.setCampus( campus );

        repository.save( location );
    }

    @Override
    public void removeLocation( int id ){
        Location location = this.getLocationById( id );

        repository.delete( location );
    }
}
