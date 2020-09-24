package oss.product.infra.read;

import oss.member.model.read.Member;
import oss.order.model.read.Delivery;
import oss.order.model.read.Order;
import oss.order.model.read.OrderItem;
import oss.product.model.read.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Component
public class OrderReadRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<OrderItem> findProuctInfo(List<OrderItem> orderItems) {

		StringBuilder query = new StringBuilder();
		if (orderItems == null || orderItems.size() == 0) 
			return Collections.emptyList();

		List<OrderItem> actualOrderItems = orderItems.stream().map(orderItem -> {
			// 상품정보 호출
			query.setLength(0);
			query.append("SELECT productId, name, price, imagePath, description FROM product where productId = ? ");
			final Product product = jdbcTemplate.queryForObject(query.toString(), new Object[] { orderItem.getProductId() }, new ProductRowMapper());
			orderItem.setProduct(product);

			return orderItem;
		}).collect(toList());

		return actualOrderItems;
	}

	static LocalDateTime convertTimestampToLocalDateTime(Timestamp dateTime) {
		return dateTime.toLocalDateTime();
	}

	static class OrderRowMapper implements RowMapper<Order> {

		@Override
		public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
			final Order order = new Order();
			order.setOrderId(rs.getLong("orderId"));
			final Timestamp created = rs.getTimestamp("created");

			order.setCreated(convertTimestampToLocalDateTime(created));

			return order;
		}
	}

	static class MemberRowMapper implements RowMapper<Member> {
		@Override
		public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
			final Member member = new Member();
			member.setId(rs.getString("memberId"));
			return member;
		}
	}

	static class DeliveryRowMapper implements RowMapper<Delivery> {
		@Override
		public Delivery mapRow(ResultSet rs, int rowNum) throws SQLException {

			final String address = rs.getString("address");
			final String phone = rs.getString("phone");
			final String deliveryMessage = rs.getString("deliveryMessage");
			final Delivery delivery = new Delivery(address, phone, deliveryMessage);

			return delivery;
		}
	}

	static class OrderItemRowMapper implements RowMapper<OrderItem> {
		@Override
		public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			final Long productId = rs.getLong("productId");
			final int price = rs.getInt("price");
			final int quantity = rs.getInt("quantity");
			final OrderItem orderItem = new OrderItem(productId, price, quantity);

			return orderItem;
		}
	}

	static class ProductRowMapper implements RowMapper<Product> {
		@Override
		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			final Long productId = rs.getLong("productId");
			final String name = rs.getString("name");
			final int price = rs.getInt("price");
			final String imagePath = rs.getString("imagePath");

			final Product product = new Product(productId, name, price, imagePath);

			return product;
		}
	}
}
