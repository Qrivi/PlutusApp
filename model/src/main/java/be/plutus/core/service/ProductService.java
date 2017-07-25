package be.plutus.core.service;

import be.plutus.core.model.Product;
import be.plutus.core.model.ProductType;

import java.util.List;

public interface ProductService{

    List<Product> getAllProducts();

    List<Product> getProductsByPrice( ProductType type, double minPrice, double maxPrice );

    Product getProductById( Integer id );

    Product getProductByLabel( String label );

    Product getProductByName( String name );

    Product createProduct( String label, String name, Double price );

    void updateProduct( int id, String name, Double price );

    void removeProduct( int id );
}
