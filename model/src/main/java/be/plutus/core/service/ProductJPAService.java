package be.plutus.core.service;

import be.plutus.core.exception.DuplicateProductException;
import be.plutus.core.exception.InvalidProductIdentifierException;
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

    private final ProductRepository productRepository;

    @Autowired
    public ProductJPAService( ProductRepository productRepository ){
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByPrice( ProductType type, double minPrice, double maxPrice ){
        if( minPrice <= 0 ) minPrice = 0;
        if( maxPrice <= 0 ) maxPrice = 9999;

        if( type == null )
            return productRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual( minPrice, maxPrice );
        return productRepository.findByTypeAndPriceGreaterThanEqualAndPriceLessThanEqual( type, minPrice, maxPrice );
    }

    @Override
    public Product getProductById( Integer id ){
        if( id == null )
            throw new InvalidProductIdentifierException();
        return productRepository.findOne( id );
    }

    @Override
    public Product getProductByLabel( String label ){
        return productRepository.findByLabel( label );
    }

    @Override
    public Product getProductByName( String name ){
        return productRepository.findByName( name );
    }

    @Override
    public Product createProduct( String label, String name, Double price ){
        Product product = new Product();

        if( this.getProductByLabel( label ) != null )
            throw new DuplicateProductException( label );

        product.setLabel( label );
        product.setName( name );
        product.setPrice( price );

        return productRepository.save( product );
    }

    @Override
    public void updateProduct( int id, String name, Double price ){
        Product product = this.getProductById( id );

        Product unique = this.getProductByName( name );
        if( unique != null && unique != product )
            throw new DuplicateProductException( name );

        if( name != null )
            product.setName( name );
        if( price != null )
            product.setPrice( price );

        productRepository.save( product );
    }

    @Override
    public void removeProduct( int id ){
        Product product = this.getProductById( id );

        productRepository.delete( product );
    }
}
