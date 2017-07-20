package be.plutus.core.repository;

import be.plutus.core.model.Campus;
import be.plutus.core.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer>{

    Location findByLabel( String label );

    Location findByName( String label );

    List<Location> findByCampus( Campus campus );
}
