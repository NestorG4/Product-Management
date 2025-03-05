package elastisearchProducts.service;

import elastisearchProducts.entity.ProductElasticsearch;
import elastisearchProducts.entity.ProductMysql;
import elastisearchProducts.events.KafkaProducer;
import elastisearchProducts.repository.ProductElasticsearchRepository;
import elastisearchProducts.repository.ProductMysqlRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductMysqlRepository productMysqlRepository;

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    public ProductMysql productSave(ProductMysql productMysql){
        ProductMysql savedProduct = productMysqlRepository.save(productMysql);
        kafkaProducer.sendEventSave("product-topic", savedProduct);
        return savedProduct;
    }
    /*
    public ProductMysql productUpdate(Integer id, Double newPrice){
        ProductMysql productMysql = productMysqlRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product doesn't exist in MySQL "));
        productMysql.setPrice(newPrice);
        productMysqlRepository.save(productMysql);

        Optional<ProductElasticsearch> productElasticsearch = productElasticsearchRepository.findById(id.toString());
        if(productElasticsearch.isPresent()){
            ProductElasticsearch productToUpdate = productElasticsearch.get();
            productToUpdate.setPrice(newPrice);
            productElasticsearchRepository.save(productToUpdate);
        }else{
            throw new IllegalStateException("Product exist en MySQL but not in ElasticSearch");
        }
        return productMysql;

     */
        /*
        Optional<ProductMysql> productOpt = productMysqlRepository.findById(id);
        if(productOpt.isEmpty()){
            throw new RuntimeException("Product not found");
        }
        //Get product in Mysql and update this product
        ProductMysql product = productOpt.get();
        product.setPrice(newPrice);
        productMysqlRepository.save(product);

        //Send event to Kafka
        String message = String.format("{\"id\":%d, \"price\":%.2f}", id, newPrice);
        kafkaProducer.sendEventUpdate("product-topic", message);
        return product;

         */
    //}
}
