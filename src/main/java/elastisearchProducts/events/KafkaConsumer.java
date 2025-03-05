package elastisearchProducts.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import elastisearchProducts.entity.ProductElasticsearch;
import elastisearchProducts.repository.ProductElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {

    @Autowired
    private ProductElasticsearchRepository elasticsearchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "product-topic", groupId = "product-group")
    public void consumeEventSave(String message){
        try{
            ProductElasticsearch productElasticsearch = objectMapper.readValue(message, ProductElasticsearch.class);
            elasticsearchRepository.save(productElasticsearch);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error processing message Kafka");
        }
    }
/*
    @KafkaListener(topics = "product-topic", groupId = "product-group")
    public void consumeEventUpdate(String message) throws JsonProcessingException{
        System.out.println("📥 Message arrived in Kafka: " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);

        String id = jsonNode.get("id").asText();
        Double price = jsonNode.get("price").asDouble();

        //Search product in ElasticSearch and updated it
        Optional<ProductElasticsearch> productOptional = elasticsearchRepository.findById(id);
        if(productOptional.isPresent()){
            ProductElasticsearch product = productOptional.get();
            product.setPrice(price);
            elasticsearchRepository.save(product);
            System.out.println("Product updated successfully");
        }else{
            System.out.println("Product not found");
            ProductElasticsearch newProduct = new ProductElasticsearch();
            newProduct.setId(id);
            newProduct.setPrice(price);
            elasticsearchRepository.save(newProduct);
            System.out.println("Product created in Elasticsearch" + newProduct);
        }
    }

 */
}
