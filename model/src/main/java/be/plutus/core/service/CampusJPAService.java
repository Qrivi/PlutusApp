package be.plutus.core.service;

import be.plutus.core.exception.DuplicateCampusException;
import be.plutus.core.exception.InvalidCampusIdentifierException;
import be.plutus.core.model.Campus;
import be.plutus.core.repository.CampusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CampusJPAService implements CampusService{

    private final CampusRepository campusRepository;

    @Autowired
    public CampusJPAService( CampusRepository campusRepository ){
        this.campusRepository = campusRepository;
    }

    @Override
    public List<Campus> getAllCampuses(){
        return campusRepository.findAll();
    }

    @Override
    public List<Campus> getCampusesByCity( String city ){
        return campusRepository.findByCity( city );
    }

    @Override
    public Campus getCampusById( Integer id ){
        if( id == null )
            throw new InvalidCampusIdentifierException();
        return campusRepository.findOne( id );
    }

    @Override
    public Campus getCampusByLabel( String label ){
        return campusRepository.findByLabel( label );
    }

    @Override
    public Campus getCampusByName( String name ){
        return campusRepository.findByName( name );
    }

    @Override
    public Campus createCampus( String label, String name, Double latitude, Double longitude, String address, Integer postcode, String city, String phone, String email, String website ){
        Campus campus = new Campus();

        if( this.getCampusByLabel( label ) != null )
            throw new DuplicateCampusException( label );

        if( this.getCampusByName( name ) != null )
            throw new DuplicateCampusException( name );

        campus.setLabel( label );
        campus.setName( name );
        campus.setLatitude( latitude );
        campus.setLongitude( longitude );
        campus.setAddress( address );
        campus.setPostcode( postcode );
        campus.setCity( city );
        campus.setPhone( phone );
        campus.setEmail( email );
        campus.setWebsite( website );

        return campusRepository.save( campus );
    }

    @Override
    public void updateCampus( int id, String name, Double latitude, Double longitude, String address, Integer postcode, String city, String phone, String email, String website ){
        Campus campus = this.getCampusById( id );

        Campus unique = this.getCampusByName( name );
        if( unique != null && unique != campus )
            throw new DuplicateCampusException( name );

        if( name != null )
            campus.setName( name );
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

        campusRepository.save( campus );
    }

    @Override
    public void removeCampus( int id ){
        Campus campus = this.getCampusById( id );

        campusRepository.delete( campus );
    }
}
