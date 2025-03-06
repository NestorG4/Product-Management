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

    @KafkaListener(topics = "product-update-topic", groupId = "product-group")
    public void consumeEventUpdate(String message) throws JsonProcessingException{
        System.out.println("📥 Message arrived in Kafka: " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);

        String id = jsonNode.get("id").asText();
        Double price = jsonNode.get("price").asDouble();

        Optional<ProductElasticsearch> productOptional = elasticsearchRepository.findById(id);
        if(productOptional.isPresent()){
            ProductElasticsearch product = productOptional.get();
            product.setPrice(price);
            elasticsearchRepository.save(product);
            System.out.println("Product updated successfully");
        }else{
            throw new IllegalArgumentException("Product exists in Mysql but not in Elasticsearch");
        }
    }

    @KafkaListener(topics = "product-update-complete-topic", groupId = "product-group")
    public void consumeEventUpdateComplete(String message) throws  JsonProcessingException{
        try{
            ProductElasticsearch productElasticsearch = objectMapper.readValue(message, ProductElasticsearch.class);
            elasticsearchRepository.save(productElasticsearch);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error processing message kafka");
        }

    }

    @KafkaListener(topics = "product-delete-topic", groupId = "product-group")
    public void consumeEventDelete(String message) throws JsonProcessingException{
        System.out.println("📥 Message arrived in Kafka: " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(message);
        String id = jsonNode.get("id").asText();
        if (elasticsearchRepository.existsById(id)) {
            elasticsearchRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("Product to delete not found in Elasticsearch");
        }
    }
}
