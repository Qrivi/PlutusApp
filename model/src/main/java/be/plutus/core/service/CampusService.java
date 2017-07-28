package be.plutus.core.service;

import be.plutus.core.model.Campus;
import be.plutus.core.model.Label;

import java.util.List;

public interface CampusService{

    List<Campus> getAllCampuses();

    List<Campus> getCampusesByCity( String city );

    Campus getCampusById( Integer id );

    Campus getCampusByLabel( Label label );

    Campus createCampus( Label label, Double latitude, Double longitude, String address, Integer postcode, String city, String phone, String email, String website );

    void updateCampus( int id, Double latitude, Double longitude, String address, Integer postcode, String city, String phone, String email, String website );

    void removeCampus( int id );
}
