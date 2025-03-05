package elastisearchProducts.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elastisearchProducts.entity.ProductMysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

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
    /*
    public void sendEventUpdate(String topic, String message){
        kafkaTemplate.send(topic, message);
    }
     */
}
