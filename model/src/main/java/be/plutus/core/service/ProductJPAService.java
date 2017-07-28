package be.plutus.core.service;

import be.plutus.core.exception.DuplicateProductException;
import be.plutus.core.exception.InvalidProductIdentifierException;
import be.plutus.core.model.Label;
import be.plutus.core.model.Product;
import be.plutus.core.model.ProductType;
import be.plutus.core.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductJPAService implements ProductService{

    private final ProductRepository repository;

    @Autowired
    public ProductJPAService( ProductRepository repository ){
        this.repository = repository;
    }

    @Override
    public List<Product> getAllProducts(){
        return repository.findAll();
    }

    @Override
    public List<Product> getProductsByPrice( ProductType type, double minPrice, double maxPrice ){
        if( minPrice <= 0 ) minPrice = 0;
        if( maxPrice <= 0 ) maxPrice = 9999;

        if( type == null )
            return repository.findByPriceGreaterThanEqualAndPriceLessThanEqual( minPrice, maxPrice );
        return repository.findByTypeAndPriceGreaterThanEqualAndPriceLessThanEqual( type, minPrice, maxPrice );
    }

    @Override
    public Product getProductById( Integer id ){
        if( id == null )
            throw new InvalidProductIdentifierException();
        return repository.findOne( id );
    }

    @Override
    public Product getProductByLabel( Label label ){
        return repository.findByLabel( label );
    }

    @Override
    public Product createProduct( Label label, Double price ){
        Product product = new Product();

        if( this.getProductByLabel( label ) != null )
            throw new DuplicateProductException( label );

        product.setLabel( label );
        product.setPrice( price );

        return repository.save( product );
    }

    @Override
    public void updateProduct( int id, Double price ){
        Product product = this.getProductById( id );

        if( price != null )
            product.setPrice( price );

        repository.save( product );
    }

    @Override
    public void removeProduct( int id ){
        Product product = this.getProductById( id );

        repository.delete( product );
    }
}
