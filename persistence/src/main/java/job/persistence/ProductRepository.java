package job.persistence;

import job.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query(value = "SELECT * FROM product WHERE product.name=:name AND product.brand=:brand AND product.description=:description", nativeQuery = true)
    Optional<Product> findProductByNameBrandAndDescription(@Param("name") String name, @Param("brand") String brand, @Param("description") String description);

}
