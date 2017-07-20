package be.plutus.core.repository;

import be.plutus.core.model.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Integer>{

    Campus findByLabel( String label );

    Campus findByName( String label );

    List<Campus> findByCity( String city );
}
