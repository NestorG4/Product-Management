package elastisearchProducts.controller;

import elastisearchProducts.entity.ProductMysql;
import elastisearchProducts.repository.ProductMysqlRepository;
import elastisearchProducts.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping("/save")
    public ResponseEntity<ProductMysql> createProduct(@RequestBody ProductMysql productMysql){
        ProductMysql savedProduct = service.productSave(productMysql);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
/*
    @PutMapping("/{id}/price")
    public ResponseEntity<ProductMysql> priceUpdate(@PathVariable Integer id, @RequestBody Map<String, Double> request){
        Double newPrice = request.get("price");
        ProductMysql updateProduct = service.productUpdate(id, newPrice);
        return ResponseEntity.ok(updateProduct);
    }

 */
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
