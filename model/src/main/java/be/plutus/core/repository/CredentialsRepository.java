package be.plutus.core.repository;

import be.plutus.core.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Integer>{

    Credentials findByCardNumberIgnoreCase( String cardNumber );
}
