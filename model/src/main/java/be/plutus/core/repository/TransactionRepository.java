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

    Transaction findByCardAndTimestamp( Card card, ZonedDateTime timestamp );

    List<Transaction> findByTimestamp( ZonedDateTime timestamp );

    List<Transaction> findByCard( Card card );

    List<Transaction> findByLocation( Location location );

    List<Transaction> findByCardAndLocation( Card card, Location location );

    List<Transaction> findByCardAndTimestampBetween( Card card, ZonedDateTime after, ZonedDateTime before );
}
