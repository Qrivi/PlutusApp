package be.plutus.core.repository;

import be.plutus.core.model.Product;
import be.plutus.core.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

    Product findByLabel( String label );

    Product findByName( String name );

    List<Product> findByPriceGreaterThanEqual( double minPrice );

    List<Product> findByPriceLessThanEqual( double maxPrice );

    List<Product> findByPriceGreaterThanEqualAndPriceLessThanEqual( double minPrice, double maxPrice );

    List<Product> findByTypeAndPriceGreaterThanEqual( ProductType type, double minPrice );

    List<Product> findByTypeAndPriceLessThanEqual( ProductType type, double maxPrice );

    List<Product> findByTypeAndPriceGreaterThanEqualAndPriceLessThanEqual( ProductType type, double minPrice, double maxPrice );
}
