package be.plutus.core.repository;

import be.plutus.core.model.Request;
import be.plutus.core.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer>{

    List<Request> findByToken( Token token );
}
