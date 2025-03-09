package elastisearchProducts.service;

import elastisearchProducts.entity.ProductElasticsearch;
import elastisearchProducts.entity.ProductMysql;
import elastisearchProducts.events.KafkaProducer;
import elastisearchProducts.repository.ProductElasticsearchRepository;
import elastisearchProducts.repository.ProductMysqlRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductMysqlRepository productMysqlRepository;

    @Autowired
    private KafkaProducer kafkaProducer;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public List<ProductMysql> listProducts(){
        return productMysqlRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public ProductMysql productSave(ProductMysql productMysql){
        logger.info("Starting transaction to save product with ID: {}", productMysql.getId());
        ProductMysql savedProduct = productMysqlRepository.save(productMysql);
        kafkaProducer.sendEventSave("product-topic", savedProduct);
        logger.info("Transaction completed save product with ID {}", productMysql.getId());
        return savedProduct;
    }

    public ProductMysql findByIdProduct(Integer id){
        return productMysqlRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product doesn't exist in MySql"));
    }

    @Transactional
    public ProductMysql updatePrice(Integer id, Double newPrice){
        logger.info("Starting transaction to update price with ID: {}", id);
        ProductMysql productMysql = findByIdProduct(id);
        productMysql.setPrice(newPrice);
        productMysqlRepository.save(productMysql);
        kafkaProducer.sendEventUpdate("product-update-topic", id, newPrice);
        logger.info("Transaction completed for price with ID {}", id);
        return productMysql;
    }

    @Transactional
    public void updateProduct(Integer id, ProductMysql updatedProduct){
        logger.info("Starting transaction to update product with ID: {}", id);
        ProductMysql existingProduct = findByIdProduct(id);
        updatedProduct.setId(existingProduct.getId());
        productMysqlRepository.save(updatedProduct);
        kafkaProducer.sendEventUpdateComplete("product-update-complete-topic", id, updatedProduct);
        logger.info("Transaction completed for product with ID {}", id);
    }

    @Transactional
    public void deleteProduct(Integer id){
        logger.info("Starting transaction to delete product with ID: {}", id);
        if (!productMysqlRepository.existsById(id)){
            throw new EntityNotFoundException("Product doesn't exist in MySql");
        }
        productMysqlRepository.deleteById(id);
        kafkaProducer.sendEventDelete("product-delete-topic", id);
        logger.info("Transaction completed to delete product with ID: {}", id);
    }

}
