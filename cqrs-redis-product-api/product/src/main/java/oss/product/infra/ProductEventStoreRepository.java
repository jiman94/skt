package oss.product.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Repository
public class ProductEventStoreRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 상품 unique id 생성
	 * @return
	 */
	public synchronized Long createProductId() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE sequence SET productId=LAST_INSERT_ID(productId+1)");

		jdbcTemplate.update(query.toString());

		query.setLength(0);

		query.append(" SELECT LAST_INSERT_ID() ");

		return jdbcTemplate.queryForObject(query.toString(), Long.class);
	}
}
