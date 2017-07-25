package be.plutus.core.service;

import be.plutus.core.model.Card;
import be.plutus.core.model.Location;
import be.plutus.core.model.Product;
import be.plutus.core.model.Transaction;

import java.time.ZonedDateTime;
import java.util.List;

public interface TransactionService{
    List<Transaction> getAllTransactions();

    List<Transaction> getTransactionsByCard( Card card );

    List<Transaction> getTransactionsByLocation( Location location );

    List<Transaction> getTransactionsByTimestamp( ZonedDateTime timestamp );

    List<Transaction> getTransactionsByCardAndLocation( Card card, Location location );

    List<Transaction> getTransactionsByCardAndTimestamp( Card card, ZonedDateTime after, ZonedDateTime before );

    Transaction getTransactionById( Integer id );

    Transaction getTransactionByCardAndTimestamp( Card card, ZonedDateTime timestamp );

    Transaction createTransaction( ZonedDateTime timestamp, Card card, Location location, List<Product> products );

    void updateTransactionProducts( int id, List<Product> products );

    void addTransactionProducts( int id, List<Product> products );

    void addTransactionProduct( int id, Product product );

    void removeTransaction( int id );
}
