package be.plutus.core.repository;

import be.plutus.core.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

    Product findByLabel( String label );

    Product findByName( String label );

    List<Product> findByPriceLessThan( double price );

    List<Product> findByPriceGreaterThan( double price );
}
