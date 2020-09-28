package oss.order.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import oss.product.model.Product;

/**
 * Created by jaceshim on 2017. 3. 28..
 */
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class OrderItem {

	/** 주문 상품 정보 */
	private Product product;

	/** 주문수량 */
	private int quantity;

	public OrderItem(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public void merge(OrderItem orderItem) {
		this.quantity += orderItem.quantity;
	}

	public boolean isProductEqual(OrderItem orderItem) {
		return this.product == orderItem.product;
	}
}
