package elastisearchProducts.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import elastisearchProducts.entity.ProductElasticsearch;
import elastisearchProducts.repository.ProductElasticsearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private ProductElasticsearchRepository elasticsearchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "product-topic", groupId = "product-group")
    public void consumeEventSave(String message){
        logger.info("Message arrived in Kafka in Save: {}", message);
        try{
            ProductElasticsearch productElasticsearch = objectMapper.readValue(message, ProductElasticsearch.class);
            String productName = productElasticsearch.getProductName() != null ? productElasticsearch.getProductName() : "";
            String description = productElasticsearch.getDescription() != null ? productElasticsearch.getDescription() : "";
            productElasticsearch.setSuggest(new Completion(new String[]{
                    productElasticsearch.getProductName(),
                    productElasticsearch.getDescription()
            }));
            elasticsearchRepository.save(productElasticsearch);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error processing message Kafka");
        }
    }

    @KafkaListener(topics = "product-update-topic", groupId = "product-group")
    public void consumeEventUpdate(String message) throws JsonProcessingException{
        logger.info("Message arrived in Kafka in Update price: {}", message);
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
        logger.info("Message arrived in Kafka in Update: {}", message);
        try{
            ProductElasticsearch productElasticsearch = objectMapper.readValue(message, ProductElasticsearch.class);
            elasticsearchRepository.save(productElasticsearch);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error processing message kafka");
        }

    }

    @KafkaListener(topics = "product-delete-topic", groupId = "product-group")
    public void consumeEventDelete(String message) throws JsonProcessingException{
        logger.info("Message arrived in Kafka in Delete: {}", message);
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
