package oss.product.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import oss.core.event.AbstractEventProjector;
import oss.product.model.event.ProductCreated;
import oss.product.model.event.ProductInventoryDecreased;
import oss.product.model.event.ProductInventoryIncreased;
import oss.product.model.event.ProductNameChanged;
import oss.product.model.event.ProductPriceChanged;

/**
 * Created by jaceshim on 2017. 4. 19..
 */
@Component
@Slf4j
public class ProductEventProjector extends AbstractEventProjector {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 상품 등록 projection
	 * @param event
	 */
	public void execute(ProductCreated event) {
		log.debug("event : {}", event.toString());
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO product (");
		query.append(" productId, name, price, inventory, imagePath, description, created ");
		query.append(") VALUES ( ");
		query.append(" ?, ?, ?, ?, ?, ?, ? ) ");

		jdbcTemplate.update(query.toString(),
			event.getProductId(),
			event.getName(),
			event.getPrice(),
			event.getInventory(),
			event.getImagePath(),
			event.getDescription(),
			convertLocalDateTimeToTimestamp(event.getCreated()));
	}

	/**
	 * 상품 명 변경 projection
	 *
	 * @param event
	 */
	public void execute(ProductNameChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET name = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getName(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}

	/**
	 * 상품 가격 변경 projection
	 *
	 * @param event
	 */
	public void execute(ProductPriceChanged event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET price = ? ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getPrice(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}

	/**
	 * 상품 수량 감소 projection
	 *
	 * @param event
	 */
	public void execute(ProductInventoryDecreased event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET inventory = (inventory - ? ) ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getInventory(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}

	/**
	 * 상품 수량 증가 projection
	 *
	 * @param event
	 */
	public void execute(ProductInventoryIncreased event) {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE product ");
		query.append(" SET inventory = (inventory + ? ) ");
		query.append("    ,updated = ?");
		query.append("WHERE productId = ?");

		jdbcTemplate.update(query.toString(), event.getInventory(), convertLocalDateTimeToTimestamp(event.getUpdated()), event.getProductId());
	}
}
