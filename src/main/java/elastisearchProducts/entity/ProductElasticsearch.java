package elastisearchProducts.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "product_index")
public class ProductElasticsearch {

    @Id
    private String id;
    private String productName;
    private String quality;
    private Double price;
}
