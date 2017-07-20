package be.plutus.core.repository;

import be.plutus.core.model.Card;
import be.plutus.core.model.Location;
import be.plutus.core.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

    Transaction findByTimestamp( ZonedDateTime timestamp );

    Transaction findByTimestampBefore( ZonedDateTime before );

    Transaction findByTimestampAfter( ZonedDateTime after );

    Transaction findByTimestampBetween( ZonedDateTime after, ZonedDateTime before );

    List<Transaction> findByCard( Card card );

    List<Transaction> findByLocation( Location location );
}
