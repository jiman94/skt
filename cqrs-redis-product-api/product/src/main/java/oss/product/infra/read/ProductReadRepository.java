package oss.product.infra.read;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import oss.product.model.read.Product;

/**
 * Created by jaceshim on 2017. 3. 29..
 */
@Repository
public interface ProductReadRepository extends JpaRepository<Product, Long> {

	Product findByProductId(Long productId);
}
