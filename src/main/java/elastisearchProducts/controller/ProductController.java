package elastisearchProducts.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import elastisearchProducts.entity.Product;
import elastisearchProducts.service.ElasticSearchService;
import elastisearchProducts.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public Iterable<Product> findAll(){
        return service.getAllProducts();
    }

    @PostMapping("/insert")
    public Product insertProduct(@RequestBody Product product){
        return service.insertProduct(product);
    }
    /*
    @GetMapping("/matchAll")
    public String matchAll() throws IOException{
        SearchResponse<Map> searchResponse = elasticSearchService.matchAllServices();
        System.out.println(searchResponse.hits().hits().toString());
        return searchResponse.hits().hits().toString();
    }

    @GetMapping("/matchAllProducts")
    public List<Product> matchAllProducts() throws IOException{
        SearchResponse<Product> searchResponse = elasticSearchService.matchAllProductsServices(){
            System.out.println(searchResponse.hits().hits().toString());
        }
    }
     */
}
