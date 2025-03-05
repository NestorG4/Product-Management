package elastisearchProducts.repository;

import elastisearchProducts.entity.ProductMysql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMysqlRepository extends JpaRepository<ProductMysql, Integer> {
}
