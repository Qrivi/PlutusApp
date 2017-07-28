package be.plutus.core.service;

import be.plutus.core.exception.DuplicateCampusException;
import be.plutus.core.exception.InvalidCampusIdentifierException;
import be.plutus.core.model.Campus;
import be.plutus.core.model.Label;
import be.plutus.core.repository.CampusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CampusJPAService implements CampusService{

    private final CampusRepository repository;

    @Autowired
    public CampusJPAService( CampusRepository repository ){
        this.repository = repository;
    }

    @Override
    public List<Campus> getAllCampuses(){
        return repository.findAll();
    }

    @Override
    public List<Campus> getCampusesByCity( String city ){
        return repository.findByCity( city );
    }

    @Override
    public Campus getCampusById( Integer id ){
        if( id == null )
            throw new InvalidCampusIdentifierException();
        return repository.findOne( id );
    }

    @Override
    public Campus getCampusByLabel( Label label ){
        return repository.findByLabel( label );
    }

    @Override
    public Campus createCampus( Label label, Double latitude, Double longitude, String address, Integer postcode, String city, String phone, String email, String website ){
        Campus campus = new Campus();

        if( this.getCampusByLabel( label ) != null )
            throw new DuplicateCampusException( label );

        campus.setLabel( label );
        campus.setLatitude( latitude );
        campus.setLongitude( longitude );
        campus.setAddress( address );
        campus.setPostcode( postcode );
        campus.setCity( city );
        campus.setPhone( phone );
        campus.setEmail( email );
        campus.setWebsite( website );

        return repository.save( campus );
    }

    @Override
    public void updateCampus( int id, Double latitude, Double longitude, String address, Integer postcode, String city, String phone, String email, String website ){
        Campus campus = this.getCampusById( id );

        if( latitude != null )
            campus.setLatitude( latitude );
        if( longitude != null )
            campus.setLongitude( longitude );
        if( address != null )
            campus.setAddress( address );
        if( postcode != null )
            campus.setPostcode( postcode );
        if( city != null )
            campus.setCity( city );
        if( phone != null )
            campus.setPhone( phone );
        if( email != null )
            campus.setEmail( email );
        if( website != null )
            campus.setWebsite( website );

        repository.save( campus );
    }

    @Override
    public void removeCampus( int id ){
        Campus campus = this.getCampusById( id );

        repository.delete( campus );
    }
}
