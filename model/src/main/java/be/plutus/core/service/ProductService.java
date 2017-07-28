package be.plutus.core.service;

import be.plutus.core.model.Label;
import be.plutus.core.model.Product;
import be.plutus.core.model.ProductType;

import java.util.List;

public interface ProductService{

    List<Product> getAllProducts();

    List<Product> getProductsByPrice( ProductType type, double minPrice, double maxPrice );

    Product getProductById( Integer id );

    Product getProductByLabel( Label label );

    Product createProduct( Label label, Double price );

    void updateProduct( int id, Double price );

    void removeProduct( int id );
}
