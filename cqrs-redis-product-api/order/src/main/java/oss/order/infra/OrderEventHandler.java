package oss.order.infra;

import oss.core.event.AbstractEventHandler;
import oss.core.event.EventStore;
import oss.core.snapshot.SnapshotRepository;
import oss.order.model.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Component
public class OrderEventHandler extends AbstractEventHandler<Order, Long> {

	@Autowired
	public OrderEventHandler(EventStore eventStore,
		SnapshotRepository snapshotRepository) {
		super(eventStore, snapshotRepository);
	}
}
