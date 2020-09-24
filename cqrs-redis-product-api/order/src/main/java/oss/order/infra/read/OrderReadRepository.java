package oss.order.infra.read;

import static java.util.stream.Collectors.toList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import oss.member.model.read.Member;
import oss.order.config.JsonCmd;
import oss.order.config.ModelMapperx;
import oss.order.model.read.Delivery;
import oss.order.model.read.Order;
import oss.order.model.read.OrderItem;
import oss.product.model.read.Product;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
@Component
public class OrderReadRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private JsonCmd jsonCmd;
	
	@Autowired
	private ModelMapperx modelMapper;
	
	private final String productUrl = "http://product.pilot.com:30001/";

	public List<Order> find() {

		StringBuilder query = new StringBuilder();
		query.append("SELECT orderId FROM `order`");
		final List<Long> orderIds = jdbcTemplate.queryForList(query.toString(), Long.class);
		if (orderIds == null || orderIds.size() == 0) {
			return Collections.emptyList();
		}

		return orderIds.stream().map((Long orderId) -> {
			Order order = new Order(orderId);

			// 배송정보 호출
			query.setLength(0);
			query.append("SELECT address, phone, deliveryMessage FROM order_delivery where orderId = ? ");
			final Delivery delivery = jdbcTemplate.queryForObject(query.toString(), new Object[] { orderId }, new DeliveryRowMapper());
			order.setDelivery(delivery);

			// 주문 아이템
			query.setLength(0);
			query.append("SELECT productId, price, quantity FROM order_items where orderId = ? ");
			final List<OrderItem> orderItems = jdbcTemplate.query(query.toString(), new Object[] { orderId }, new OrderItemRowMapper());

			final List<OrderItem> actualOrderItems = orderItems.stream().map(orderItem -> {
				// 상품정보 호출
				query.setLength(0);
				query.append("SELECT productId, name, price, imagePath FROM product where productId = ? ");
				final Product product = jdbcTemplate.queryForObject(query.toString(), new Object[] { orderItem.getProductId() }, new ProductRowMapper());
				orderItem.setProduct(product);

				return orderItem;
			}).collect(toList());

			order.setOrderItems(actualOrderItems);

			return order;
		}).collect(toList());
	}
	
	public List<Order> findOrderInfo() {

		StringBuilder query = new StringBuilder();
		query.append("SELECT orderId FROM `order`");
		final List<Long> orderIds = jdbcTemplate.queryForList(query.toString(), Long.class);
		if (orderIds == null || orderIds.size() == 0) {
			return Collections.emptyList();
		}

		return orderIds.stream().map((Long orderId) -> {
			Order order = new Order(orderId);

			// 주문 아이템
			query.setLength(0);
			query.append("SELECT productId, price, quantity FROM order_items where orderId = ? ");
			final List<OrderItem> orderItems = jdbcTemplate.query(query.toString(), new Object[] { orderId }, new OrderItemRowMapper());

			// product API 호
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON );
			String params = jsonCmd.objToJsonString(orderItems);
			HttpEntity <String> httpEntity = new HttpEntity <> (params, httpHeaders);
			ResponseEntity<String> resdata;
			resdata = restTemplate.exchange(productUrl+"api/productlist", HttpMethod.POST, httpEntity, String.class);

			List databody = (List) jsonCmd.jsonStringToObj(resdata.getBody(), List.class);

			List<OrderItem> actualOrderItems = (List<OrderItem>) databody.stream().map(data -> (OrderItem) modelMapper.map(data, OrderItem.class)).collect(Collectors.toList());
			order.setOrderItems(actualOrderItems);

			return order;
		}).collect(toList());
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
