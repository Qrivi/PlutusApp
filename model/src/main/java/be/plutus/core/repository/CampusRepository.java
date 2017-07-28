package be.plutus.core.repository;

import be.plutus.core.model.Campus;
import be.plutus.core.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Integer>{

    Campus findByLabel( Label label );

    List<Campus> findByCity( String city );
}
