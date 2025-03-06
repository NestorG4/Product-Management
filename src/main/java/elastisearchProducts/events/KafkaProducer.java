package elastisearchProducts.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elastisearchProducts.entity.ProductMysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendEventSave(String topic, ProductMysql productMysql){
        try{
            String message = objectMapper.writeValueAsString(productMysql);
            kafkaTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing product to Kafka", e);
        }
    }
    public void sendEventUpdate(String topic, Integer id,  Double newPrice){
        try{
            String message = objectMapper.writeValueAsString(Map.of(
                    "id", id,
                    "price", newPrice
            ));
            kafkaTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing update event for kafka", e);
        }
    }

    public void sendEventDelete(String topic, Integer id) {
        try{
            String message = objectMapper.writeValueAsString(Map.of(
                    "id", id
            ));
            kafkaTemplate.send(topic, message);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error serializing delete event for kafka", e);
        }
    }

    public void sendEventUpdateComplete(String topic, Integer id, ProductMysql updatedProduct) {
        try{
            String message = objectMapper.writeValueAsString(updatedProduct);
            kafkaTemplate.send(topic, message);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error serializing update complete for kafka event");
        }
    }
}
