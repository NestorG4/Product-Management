package elastisearchProducts.entity;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.suggest.Completion;

import java.util.List;


@NoArgsConstructor
@Data
@Document(indexName = "product_index")
public class ProductElasticsearch {

    @Id
    private String id;
    private String productName;
    private String quality;
    private Double price;
    private String description;
    @CompletionField
    private Completion suggest;

    public ProductElasticsearch(String id, String productName, String quality, Double price, String description, Completion suggest) {
        this.id = id;
        this.productName = productName;
        this.quality = quality;
        this.price = price;
        this.description = description;
        this.suggest = new Completion(List.of(productName, description));
    }

}
