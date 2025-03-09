package elastisearchProducts.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import elastisearchProducts.entity.ProductElasticsearch;
import elastisearchProducts.repository.ProductElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceElasticSearch {

    @Autowired
    private ProductElasticsearchRepository repository;

    public List<ProductElasticsearch> getProductsByProductName(String query) {
        return repository.findByProductNameContainingIgnoreCase(query);
    }

    public List<ProductElasticsearch> getByPriceBetween(Double min, Double max) {
        return repository.findByPriceBetween(min, max);
    }

}
