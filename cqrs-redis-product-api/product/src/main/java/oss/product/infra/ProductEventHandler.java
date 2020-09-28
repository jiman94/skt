package oss.product.infra;

import oss.core.event.AbstractEventHandler;
import oss.core.event.EventStore;
import oss.core.snapshot.SnapshotRepository;
import oss.product.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jaceshim on 2017. 3. 14..
 */
@Component
public class ProductEventHandler extends AbstractEventHandler<Product, Long> {

	@Autowired
	public ProductEventHandler(EventStore eventStore,
		SnapshotRepository snapshotRepository) {
		super(eventStore, snapshotRepository);
	}
}
