package be.plutus.core.repository;

import be.plutus.core.model.Card;
import be.plutus.core.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer>{

    Card findByCredentials( Credentials credentials );

    Card findByEmailIgnoreCase( String email );
}
