package elastisearchProducts.repository;

import elastisearchProducts.entity.ProductElasticsearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductElasticsearch, String> {

    List<ProductElasticsearch> findByProductNameContainingIgnoreCase(String query);

    @Query("{\"range\": {\"price\": {\"gte\": ?0, \"lte\": ?1}}}")
    List<ProductElasticsearch> findByPriceBetween(Double min, Double max);

    /*
    Solution in progress
    @Query("{\"suggest\": {\"product-suggest\": {\"prefix\": \"?0\", \"completion\": {\"field\": \"suggest\"}}}}")
    List<ProductElasticsearch> getAutocompleteSuggest(String prefix);
     */
}
