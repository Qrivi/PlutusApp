package be.plutus.core.repository;

import be.plutus.core.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer>{

    Card findByEmailIgnoreCase( String email );
}
