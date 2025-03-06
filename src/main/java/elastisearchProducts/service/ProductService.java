package elastisearchProducts.service;

import elastisearchProducts.entity.ProductElasticsearch;
import elastisearchProducts.entity.ProductMysql;
import elastisearchProducts.events.KafkaProducer;
import elastisearchProducts.repository.ProductElasticsearchRepository;
import elastisearchProducts.repository.ProductMysqlRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    public List<ProductMysql> listProducts(){
        return productMysqlRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public ProductMysql productSave(ProductMysql productMysql){
        ProductMysql savedProduct = productMysqlRepository.save(productMysql);
        kafkaProducer.sendEventSave("product-topic", savedProduct);
        return savedProduct;
    }

    public ProductMysql findByIdProduct(Integer id){
        return productMysqlRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product doesn't exist in MySql"));
    }

    public ProductMysql updatePrice(Integer id, Double newPrice){
        //Check if this works
        ProductMysql productMysql = findByIdProduct(id);
        productMysql.setPrice(newPrice);
        productMysqlRepository.save(productMysql);
        kafkaProducer.sendEventUpdate("product-update-topic", id, newPrice);
        return productMysql;
    }

    public void updateProduct(Integer id, ProductMysql updatedProduct){
        ProductMysql existingProduct = findByIdProduct(id);
        updatedProduct.setId(existingProduct.getId());
        productMysqlRepository.save(updatedProduct);
        kafkaProducer.sendEventUpdateComplete("product-update-complete-topic", id, updatedProduct);
    }

    public void deleteProduct(Integer id){
        if (!productMysqlRepository.existsById(id)){
            throw new EntityNotFoundException("Product doesn't exist in MySql");
        }
        productMysqlRepository.deleteById(id);
        kafkaProducer.sendEventDelete("product-delete-topic", id);
    }

}
