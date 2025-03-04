package elastisearchProducts.service;

import elastisearchProducts.entity.Product;
import elastisearchProducts.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Iterable<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product insertProduct(Product product){
        return productRepository.save(product);
    }


}
