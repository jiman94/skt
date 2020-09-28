package oss.order.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import oss.member.model.read.Member;
import oss.order.infra.OrderEventHandler;
import oss.order.infra.OrderEventStoreRepository;
import oss.order.infra.read.OrderReadRepository;
import oss.order.infra.remote.MemberInfoRepository;
import oss.order.infra.remote.ProductInfoRepository;
import oss.order.model.Delivery;
import oss.order.model.Order;
import oss.order.model.OrderItem;
import oss.order.model.command.OrderCommand;
import oss.product.model.Product;

/**
 * Created by jaceshim on 2017. 4. 6..
 */
@Service
@Slf4j
public class OrderService {
	
	@Autowired
	private OrderEventHandler orderEventHandler;

	@Autowired
	private OrderEventStoreRepository orderEventStoreRepository;

	@Autowired
	private MemberInfoRepository memberInfoRepository;

	@Autowired
	private ProductInfoRepository productInfoRepository;
	
	@Autowired
	private OrderReadRepository orderReadRepository;
	
	/**
	 * 주문
	 *
	 * @param orderCreateCommand
	 * @return
	 */
	public Order createProduct(OrderCommand.CreateOrder orderCreateCommand) {
		// DB sequence를 통해서 유일한 orderId값 생성
		final Long orderId = orderEventStoreRepository.createOrderId();
		final Delivery delivery = new Delivery(orderCreateCommand.getDelivery().getAddress(),
			orderCreateCommand.getDelivery().getPhone(),
			orderCreateCommand.getDelivery().getDeliveryMessage());

		final List<OrderItem> orderItems = orderCreateCommand.getOrderItems().stream().map(checkoutItem -> {
			final Product readProduct = productInfoRepository.findByProductId(checkoutItem.getProductId());

			final oss.product.model.Product modelProduct = new oss.product.model.Product(
				readProduct.getProductId(),
				readProduct.getName(), readProduct.getPrice(), readProduct.getInventory());

			return new OrderItem(modelProduct, checkoutItem.getQuantity());
		}).collect(Collectors.toList());

		final Member readMember = memberInfoRepository.findById(orderCreateCommand.getMemberId());

		final oss.member.model.Member modelMember = new oss.member.model.Member(readMember.getId(),
			readMember.getName(), readMember.getEmail(), readMember.getAddress());

		final Order newOrder = Order.order(orderId, modelMember, delivery, orderItems);

		orderEventHandler.save(newOrder);

		return newOrder;
	}
	
	public List<oss.order.model.read.Order> getOrderList() {
		return orderReadRepository.findOrderInfo();
	}

	
}
