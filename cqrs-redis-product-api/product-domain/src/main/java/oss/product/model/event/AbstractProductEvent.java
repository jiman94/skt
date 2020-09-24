package oss.product.model.event;

import lombok.Getter;
import oss.core.event.Event;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
@Getter
public class AbstractProductEvent implements Event<Long> {
	/** 상품 아이디 */
	protected Long productId;

	@Override
	public Long getIdentifier() {
		return this.productId;
	}
}
