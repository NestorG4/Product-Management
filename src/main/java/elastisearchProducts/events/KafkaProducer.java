package elastisearchProducts.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elastisearchProducts.entity.ProductMysql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendEventSave(String topic, ProductMysql productMysql){
        try{
            logger.info("Attempting to send product with ID: {} to Kafka topic: {}", productMysql.getId(), topic);
            String message = objectMapper.writeValueAsString(productMysql);
            kafkaTemplate.send(topic, message);
            logger.info("Successfully sent product with ID: {} to Kafka topic: {}", productMysql.getId(), topic);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing product with ID: {} to Kafka", productMysql.getId(), e);
            throw new RuntimeException("Error serializing product to Kafka", e);
        }
    }
    public void sendEventUpdate(String topic, Integer id,  Double newPrice){
        try{
            logger.info("Attempting to update price with ID: {} to Kafka topic: {}", id, topic);
            String message = objectMapper.writeValueAsString(Map.of(
                    "id", id,
                    "price", newPrice
            ));
            kafkaTemplate.send(topic, message);
            logger.info("Successfully update price with ID: {} to Kafka topic: {}", id, topic);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing price with ID: {} to Kafka", id, e);
            throw new RuntimeException("Error serializing update event for kafka", e);
        }
    }

    public void sendEventDelete(String topic, Integer id) {
        try{
            logger.info("Attempting to delete product with ID: {} to Kafka topic: {}", id, topic);
            String message = objectMapper.writeValueAsString(Map.of(
                    "id", id
            ));
            kafkaTemplate.send(topic, message);
            logger.info("Successfully delete product with ID: {} to Kafka topic: {}", id, topic);
        }catch (JsonProcessingException e){
            logger.error("Error serializing delete product with ID: {} to Kafka", id, e);
            throw new RuntimeException("Error serializing delete product for kafka", e);
        }
    }

    public void sendEventUpdateComplete(String topic, Integer id, ProductMysql updatedProduct) {
        try{
            logger.info("Attempting to update product with ID: {} to Kafka topic: {}", id, topic);
            String message = objectMapper.writeValueAsString(updatedProduct);
            kafkaTemplate.send(topic, message);
            logger.info("Successfully update to product with ID: {} to Kafka topic: {}", id, topic);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error serializing update complete for kafka event");
        }
    }
}
