
package oss.order.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Repository
public class OrderEventStoreRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 주문 unique id 생성
	 * @return
	 */
	public synchronized Long createOrderId() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE sequence SET orderId=LAST_INSERT_ID(orderId+1)");

		jdbcTemplate.update(query.toString());

		query.setLength(0);

		query.append(" SELECT LAST_INSERT_ID() ");

		return jdbcTemplate.queryForObject(query.toString(), Long.class);
	}

}