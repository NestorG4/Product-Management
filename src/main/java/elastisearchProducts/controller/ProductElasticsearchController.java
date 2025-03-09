package elastisearchProducts.controller;

import elastisearchProducts.entity.ProductElasticsearch;
import elastisearchProducts.service.ProductServiceElasticSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductElasticsearchController {

    @Autowired
    private ProductServiceElasticSearch service;

    /**
     * Searches for products by name (case-insensitive)
     * @param query partial o full product name to search for
     * @return List of products found
     */
    @GetMapping("/searchByName")
    public ResponseEntity<List<ProductElasticsearch>> getProductsByName(@RequestParam String query){
        List<ProductElasticsearch> foundProductsByName = service.getProductsByProductName(query);
        return foundProductsByName.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.OK).body(foundProductsByName);
    }

    /**
     * Searches for products by minimum and maximum values
     * @param min
     * @param max
     * @return List of products found in this range
     */
    @GetMapping("/searchByPriceBetween")
    public ResponseEntity<List<ProductElasticsearch>> getByPriceBetween(@RequestParam Double min, @RequestParam Double max){
        List<ProductElasticsearch> foundProductsByPriceBetween = service.getByPriceBetween(min, max);
        return foundProductsByPriceBetween.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.OK).body(foundProductsByPriceBetween);
    }

}
