package elastisearchProducts.controller;

import elastisearchProducts.entity.ProductMysql;
import elastisearchProducts.repository.ProductMysqlRepository;
import elastisearchProducts.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/list")
    public ResponseEntity<List<ProductMysql>> listProducts(){
        List<ProductMysql> listProducts =  service.listProducts();
        return listProducts.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.status(HttpStatus.OK).body(listProducts);
    }

    @PostMapping("/save")
    public ResponseEntity<ProductMysql> createProduct(@RequestBody ProductMysql productMysql){
        ProductMysql savedProduct = service.productSave(productMysql);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<ProductMysql> findProduct(@PathVariable Integer id){
        ProductMysql product = service.findByIdProduct(id);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<ProductMysql> updatePrice(@PathVariable Integer id, @RequestBody Map<String, Double> request){
        Double newPrice = request.get("price");
        ProductMysql updatedPrice = service.updatePrice(id, newPrice);
        return ResponseEntity.ok(updatedPrice);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct (@PathVariable Integer id, @RequestBody ProductMysql productMysql){
        service.updateProduct(id, productMysql);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id){
        service.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
