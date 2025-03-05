package elastisearchProducts.repository;

import elastisearchProducts.entity.ProductElasticsearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductElasticsearch, String> {

}
