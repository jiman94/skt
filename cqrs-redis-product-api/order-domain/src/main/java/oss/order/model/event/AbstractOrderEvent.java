package oss.order.model.event;

import oss.core.event.Event;

/**
 * Created by jaceshim on 2017. 4. 16..
 */
public abstract class AbstractOrderEvent implements Event<Long> {
	/** 주문번호 */
	protected Long orderId;

	@Override
	public Long getIdentifier() {
		return this.orderId;
	}
}
