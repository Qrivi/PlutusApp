package be.plutus.core.service;

import be.plutus.core.model.Campus;

import java.util.List;

public interface CampusService{
    List<Campus> getAllCampuses();

    List<Campus> getCampusesByCity( String city );

    Campus getCampusById( Integer id );

    Campus getCampusByLabel( String label );

    Campus getCampusByName( String name );

    Campus createCampus( String label, String name, Double latitude, Double longitude, String address, Integer postcode, String city, String phone, String email, String website );

    void updateCampus( int id, String name, Double latitude, Double longitude, String address, Integer postcode, String city, String phone, String email, String website );

    void removeCampus( int id );
}
